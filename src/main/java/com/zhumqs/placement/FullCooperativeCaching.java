
package com.zhumqs.placement;

import com.zhumqs.constants.ExperimentConstants;
import com.zhumqs.encounter.EncounterProbability;
import com.zhumqs.model.Content;
import com.zhumqs.model.MobileUser;
import com.zhumqs.predict.RequestProbability;
import com.zhumqs.utils.DataMockUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author mingqizhu
 * @date 20200103
 */
@Slf4j
@Data
public class FullCooperativeCaching {

    private double weight1;
    private double weight2;
    private int userNumber;
    private int contentNumber;
    private int capacity;
    private List<MobileUser> users;
    private List<Content> contents;
    private int[][] trustMat;

    private double[][] encounterMat;
    private double[][] requestProbabilityMat;
    private double[][] expectationMat;
    private Map<Integer, List<Integer>> cacheMap;
    private Map<Integer, Integer> placementMap;

    public FullCooperativeCaching(double weight1, double weight2, int capacity,
                                  List<MobileUser> users,
                                  List<Content> contents,
                                  int[][] trustMat) {
        this.weight1 = weight1;
        this.weight2 = weight2;
        this.capacity = capacity;
        this.users = users;
        this.contents = contents;
        this.userNumber = users.size();
        this.contentNumber = contents.size();
        this.trustMat = trustMat;
        this.cacheMap = new HashMap<>();
        this.placementMap = new HashMap<>();
    }

    private void initCacheStrategy() {
        this.expectationMat = getExpectationMatrix();
        for (int i = 0; i <  contentNumber; i++) {
            double maxExpectation = Double.MIN_VALUE;
            int cacheUserId = 0;
            for (int j = 0; j < userNumber; j++) {
                if (expectationMat[j][i] > maxExpectation ) {
                    List<Integer> list = cacheMap.get(j + 1);
                    if (list == null || list.size() < capacity) {
                        cacheUserId = j + 1;
                        maxExpectation = expectationMat[j][i];
                    }
                }
            }
            if (cacheUserId != 0) {
                List<Integer> cachedContents = cacheMap.get(cacheUserId);
                if (cachedContents == null) {
                    cachedContents = new ArrayList<Integer>();
                }
                cachedContents.add(i + 1);
                cacheMap.put(cacheUserId, cachedContents);
                placementMap.put(i + 1, cacheUserId);
            }
        }
    }

    private double[][] getExpectationMatrix() {
        EncounterProbability encounterProbability = new EncounterProbability(users, trustMat);
        this.encounterMat = encounterProbability.getEncounterMatrix(weight1, weight2);

        RequestProbability requestProbability = new RequestProbability(users, contents);
        this.requestProbabilityMat = requestProbability.getRequestProbabilityMatrix();

        double[][] expectationMat = new double[userNumber][contentNumber];
        for (int i = 0; i < userNumber; i++) {
            for (int j = 0; j < contentNumber; j++) {
                double expectation = 0.0;
                for (int k = 0; k < userNumber; k++) {