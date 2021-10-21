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
        List<MobileUser> users = DataMockUtils.mockUserInfo(ExperimentConstants.DEFAULT_USER_NUMBER);
        List<Content> contents = DataMockUtils.mockContents(ExperimentConstants.DEFAULT_CONTENT_NUMBER);

        List<Double> cacheHitRatioForFullCooperation = new Ar