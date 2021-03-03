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
        this.placementMap 