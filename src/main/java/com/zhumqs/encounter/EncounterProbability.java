
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