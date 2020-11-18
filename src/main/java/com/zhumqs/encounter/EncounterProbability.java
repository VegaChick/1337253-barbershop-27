
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