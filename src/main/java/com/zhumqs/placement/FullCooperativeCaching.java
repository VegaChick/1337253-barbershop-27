
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