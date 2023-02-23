
package com.zhumqs.utils;

import com.alibaba.fastjson.JSON;
import com.zhumqs.constants.ExperimentConstants;
import com.zhumqs.model.Content;
import com.zhumqs.model.MobileUser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mingqizhu
 * @date 20200102
 */
@Slf4j
public class DataMockUtils {

    public static List<MobileUser> mockUserInfo(int userNumber) {
        List<MobileUser> users = new ArrayList<MobileUser>();
        for (int i = 1; i <= userNumber; i++) {
            MobileUser user = new MobileUser();
            user.setUserId(i);
            user.setInstitute(RandomUtils.getRandomInterval(1, 30));
            user.setCountry(RandomUtils.getRandomInterval(1, 30));
            user.setCity(RandomUtils.getRandomInterval(1, 30));
            user.setAge(RandomUtils.getRandomInterval(20, 50));
            user.setSex(RandomUtils.getRandom(2));
            int size = RandomUtils.getRandomInterval(3, 6);
            List<Integer> interests = new ArrayList<Integer>();
            for (int j = 0; j < size; j++) {
                interests.add(RandomUtils.getRandomInterval(1, 30));
            }
            user.setInterests(interests);
            users.add(user);
        }
        return users;
    }

    public static int[][] mockTrustRelationship(double socialWeight, int userNumber) {
        int[][] trustMat = new int[userNumber][userNumber];
        for (int i = 0; i < userNumber; i++) {
            for  (int j = 0; j < userNumber; j++)  {
                trustMat[i][j] = Math.random() < socialWeight ? 1 : 0;
            }
        }
        return trustMat;
    }
