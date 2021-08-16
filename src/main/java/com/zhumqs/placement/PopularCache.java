
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
@Data
@Slf4j
public class PopularCache {
    private int userNumber;
    private int contentNumber;
    private int capacity;
    private List<MobileUser> users;
    private List<Content> contents;
    private int[][] trustMat;

    private Map<Integer, List<Integer>> cacheMap;
    private Map<Integer, Integer> placementMap;

    public PopularCache(List<MobileUser> users, List<Content> contents, int capacity, int[][] trustMat) {
        this.users = users;
        this.contents = contents;
        this.capacity = capacity;
        this.userNumber = users.size();
        this.contentNumber = contents.size();
        this.trustMat = trustMat;
        this.cacheMap = new HashMap<>();
        this.placementMap = new HashMap<>();
    }

    private void initCacheStrategy() {
        List<Content> sortedContents = contents.stream().sorted((u1, u2)
                -> u2.getPopularity().compareTo(u1.getPopularity())).collect(Collectors.toList());

        // 每个用户尽可能多的缓存流行内容
        int index = 0;
        for (int i = 1; i <= userNumber; i++) {
            // 缓存足够且有剩余
            if (index >= sortedContents.size()) {
                break;
            }
            List<Integer> cachedContentIds = new ArrayList<>();
            for (int j = 0; j < capacity; j++) {
                // 缓存足够且有剩余
                if (index >= sortedContents.size()) {
                    break;
                }
                int contentId = sortedContents.get(index).getContentId();
                cachedContentIds.add(contentId);
                placementMap.put(contentId, i);
                index++;
            }
            cacheMap.put(i, cachedContentIds);
        }
    }

    public double getCacheHitRatio() {
        initCacheStrategy();
        EncounterProbability encounterProbability = new EncounterProbability(users, trustMat);
        double[][] encounterMat = encounterProbability.getEncounterMatrix(ExperimentConstants.DEFAULT_WEIGHT1,
                ExperimentConstants.DEFAULT_WEIGHT2);

        double d1 = 0.0, d2 = 0.0;
        for (int i = 1; i <= userNumber; i++) {
            for (int j = 1; j <= contentNumber; j++) {
                double requestProbability = contents.get(j - 1).getPopularity();
                if (placementMap.containsKey(j)) {
                    int cachedUserId = placementMap.get(j);
                    double encounter = encounterMat[i - 1][cachedUserId - 1];
                    d1 += encounter * requestProbability;
                }