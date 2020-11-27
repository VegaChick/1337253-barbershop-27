
package com.zhumqs.encounter;


import com.zhumqs.constants.ExperimentConstants;
import com.zhumqs.model.MobileUser;
import com.zhumqs.utils.DataMockUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author mingqizhu
 * @date 20200102
 */
@Slf4j
public class EncounterProbability {

    private List<MobileUser> users;
    private int[][] trustMat;

    public EncounterProbability(List<MobileUser> users, int[][] trustMat) {
        this.users = users;
        this.trustMat = trustMat;
    }

    /**
     * 计算用户的遭遇概率并获得用户遭遇概率矩阵
     * @param weight1 社会关联性和用户相似性之间的权重比例
     * @param weight2 社会关联中社会关系和朋友数量之间的权重比例
     * @return
     */
    public double[][] getEncounterMatrix(double weight1, double weight2) {
        int userNumber = users.size();
        double[][] encounterMat = new double[userNumber][userNumber];
        for (int i = 0; i < userNumber; i++) {
            for (int j = 0; j < userNumber; j++) {
                if (i == j) {
                    encounterMat[i][j] = 1;
                } else {
                    encounterMat[i][j] = weight1 * getSocialRelevance(i + 1, j + 1, weight2)
                            + (1 -  weight1) * getUserSimilarity(i + 1, j + 1);
                }
            }
        }
        return encounterMat;
    }

    private double getUserSimilarity(int userId1, int userId2) {
        double[] feature1 = normalizationUserFeature(userId1);
        double[] feature2 = normalizationUserFeature(userId2);
        double d1 = 0.0, d2 = 0.0, d3 = 0.0;
        for (int i = 0; i < feature1.length; i++) {
            d1 += feature1[i] * feature2[i];
            d2 += feature1[i] * feature1[i];
            d3 += feature2[i] * feature2[i];
        }
        return d1 / (d2 * d3);
    }

    private double[] normalizationUserFeature(int userId) {
        double[] normalizedFeature = new double[6];
        MobileUser user = users.get(userId - 1);
        normalizedFeature[0] = (user.getAge() -  20) * 1.0 / 30;
        normalizedFeature[1] = 0.5;
        normalizedFeature[2] = user.getCity() * 1.0 /  30;
        normalizedFeature[3] = user.getInstitute() * 1.0 / 30;
        normalizedFeature[4] = user.getCity() * 1.0 / 30;

        List<Integer> interests = user.getInterests();
        int count = 0;
        for (int i : interests) {
            count += i;
        }
        normalizedFeature[5] = count * 1.0 / (interests.size() * 30);

        return normalizedFeature;
    }

    private double getSocialRelevance(int userId1, int userId2, double wight) {
        double d1 = getTieRelevance(userId1, userId2);
        double d2 = getNumberRelevance(userId1, userId2);
        return wight * d1 / (1 + d1) + (1 - wight) * d2 / (1 + d2);
    }

    private double getNumberRelevance(int userId1, int userId2) {
        List<Integer> ties1 = getSocialTie(userId1);
        List<Integer> ties2 = getSocialTie(userId2);

        Set<Integer> friendSet1 = new HashSet<>();
        Set<Integer> friendSet2 = new HashSet<>();
        for (Integer i : ties1) {
            friendSet1.addAll(getSocialTie(i));
        }
        for (Integer i : ties2) {
            friendSet2.addAll(getSocialTie(i));
        }

        return ((ties1.size() * 1.0 / friendSet1.size())) * ((ties2.size() * 1.0 / friendSet2.size()));
    }

    private double getTieRelevance(int userId1, int userId2) {
        List<Integer> ties1 = getSocialTie(userId1);
        List<Integer> ties2 = getSocialTie(userId2);
        return getIntersection(ties1, ties2).size() * 1.0 / getUnionSet(ties1, ties2).size();
    }

    private List<Integer> getSocialTie(int userId) {
        List<Integer> ties = new ArrayList<Integer>();
        for (int i = 0; i < trustMat[0].length; i++) {
            if (trustMat[userId - 1][i] == 1) {
                ties.add(i + 1);
            }
        }
        return ties;
    }

    private Set<Integer> getIntersection(List<Integer> l1, List<Integer> l2) {
        l1.retainAll(l2);
        return new HashSet<>(l1);
    }

    private Set<Integer> getUnionSet(List<Integer> l1, List<Integer> l2) {
        Set<Integer> unionSet = new HashSet<Integer>(l1);
        unionSet.addAll(l2);
        return unionSet;
    }

    public static void main(String[] args) {
        EncounterProbability probability = new EncounterProbability(DataMockUtils.mockUserInfo(ExperimentConstants.DEFAULT_USER_NUMBER),
                DataMockUtils.mockTrustRelationship(ExperimentConstants.DEFAULT_SOCIAL_WEIGHT, ExperimentConstants.DEFAULT_USER_NUMBER));
        List<Integer> tie = probability.getSocialTie(1);
        log.info(tie.toString());
        List<Integer> tie1 = probability.getSocialTie(2);
        log.info(tie1.toString());

        Set<Integer> unionSet = probability.getUnionSet(tie, tie1);
        log.info("union set is {}",unionSet.toString());

        Set<Integer> intersection = probability.getIntersection(tie, tie1);
        log.info("intersection is {}", intersection);

        double[][] encounterMatrix = probability.getEncounterMatrix(ExperimentConstants.DEFAULT_WEIGHT1,
                ExperimentConstants.DEFAULT_WEIGHT2);
        log.info(Arrays.deepToString(encounterMatrix));
    }
}