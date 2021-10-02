package com.zhumqs.placement;

import com.zhumqs.constants.ExperimentConstants;
import com.zhumqs.model.Content;
import com.zhumqs.model.MobileUser;
import com.zhumqs.utils.DataMockUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mingqizhu
 * @date 20200105
 */
@Slf4j
@Data
public class Simulator {

    private static void testSocialImpact() {

        log.info("<------Impact of social relation------>");
        List<M