package com.zhumqs.predict;

import com.zhumqs.model.Content;
import com.zhumqs.model.MobileUser;
import com.zhumqs.utils.DataMockUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author mingqizhu
 * @date 20200103
 */
@Slf4j
public class RequestProbability {
    private List<MobileUser> users;
    private List<Content> contents;

    public RequestProbability(List<MobileUser> users, List<Content> contents) {
        this.users = users;
        this.contents = contents;
    }

    public double[][] getRequestProbabilityMatrix() {
        int userNumber = users.size();
        int contentNumber = contents.size();
        double[][] requestProbabilityMat = new double[userNumber][contentNumber];
        for (int i = 0; i < userNumber;