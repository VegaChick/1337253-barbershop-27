
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