package com.zhumqs.placement;

import com.zhumqs.constants.ExperimentConstants;
import com.zhumqs.encounter.EncounterProbability;
import com.zhumqs.model.Content;
import com.zhumqs.model.MobileUser;
import com.zhumqs.utils.DataMockUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mingqizhu
 * @date 20200105
 */
@Slf4j
@Data
public class FairCache {
    private int userNumber;
    private int contentNumber;
    private int capacity;
    private List<MobileUser> users;
    private List<Content> contents;
    private int[][] trustMat;

    private Map<Integer, List<Integer>> cacheMap;
    private Map<Integer, List<Integer>> placementMap;

    public FairCache(List<MobileUser> users, List<Content> contents, int capacity, int[][] trustMat) {
        this.users = users;
        this.contents = contents;
        this.capacity = capacity;
        this.trustMat = trustMat;
        this.userNumber = users.size();
        this.contentNumber = contents.size();
        this.cacheMap = new HashMap<>();
        this.placementMap = new HashMap<>();
    }

    private void initCacheStrategy() {
        List<Content> sortedContents = contents.stream().sorted((u1, u2)
                -> u2.getPopularity().compareTo(u1.getPopularity())).collect(Collectors.toList());
        int totalCacheCapacity  = capacity * userNumber;
        List<Integer> cachedContentIds = new ArrayList<>();

        for (int i = 0; i < totalCacheCapacity; i++) {
            if (i >= contentNumber) {
                break;
            }
            int contentId = sortedContents.get(i).getContentId();
            cachedContentIds.add(contentId);
        }

        // cacheMap
        List<Integer> cachedUserIds = new ArrayList<>();
        for (int i = 1; i <= userNumber; i++) {
            cacheMap.put(i, cachedContentIds);
            cachedUserIds.add(i);
        }
        // placementMap
        for (int i : cachedContentIds) {
            placementMap.put(i, cachedUserIds);
        }
    }

    public double getCacheHitRatio() {
        initCacheStrategy();
        EncounterProbability encounterProbability = new EncounterProbability(users, trustMat);
        double[][] encounterMat = encounterProbability.getEncounterMatrix(ExperimentConstants.DEFAULT_WEIGHT1,
                ExperimentConstants.DEFAULT_WEIGHT2);

        double d1 = 0.0, d2 = 0.0;
        for (int i = 0; i < userNumber; i++) {
            List<Integer> cachedContentIds = cacheMap.get(i + 1);
            for (int j = 0; j < contentNumber; j++) {
                double requestProbability = contents.get(j).getPopularity();
                d2 += requestProbability;
                if (cachedContentIds.contains(j + 1)) {
                    // 计算与其他缓存用户的平均遭遇概率
                    double totalEncounter = 0.0;
                    for (int k = 0; k < userNumber; k++) {
                        if (i != k) {
                            totalEncounter += encounterMat[i][k];
                        }
                    }
                    double averageEncounter = totalEncounter / (userNumber - 1);
                    d1 += averageEncounter * requestProbability;
                }
            }
        }

        return d1 / d2;
    }

    public static void main(String[] args) {
        List<MobileUser> users = DataMockUtils.mockUserInfo(ExperimentConstants.DEFAULT_USER_NUMBER);
        List<Content> contents = DataMockUtils.mockContents(5000);
        int[][] trustMat = DataMockUtils.mockTrustRelationship(ExperimentConstants.DEFAULT_SOCIAL_WEIGHT, ExperimentConstants.DEFAULT_USER_NUMBER);
        FairCache fairCache = new FairCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
        double cacheHitRatio = fairCache.getCacheHitRatio();
        log.info("Cache hit ratio of fair caching is: {}" ,cacheHitRatio);
    }
}
