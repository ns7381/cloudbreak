package com.sequenceiq.it.cloudbreak.newway.action;

import java.util.Map;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.assertion.AssertionV2;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.wait.WaitUtil;

public class WaitAndCheckClusterAndStackAvailablityAction implements AssertionV2<StackEntity> {

    private WaitUtil waitUtil;

    public WaitAndCheckClusterAndStackAvailablityAction(WaitUtil waitUtil) {
        this.waitUtil = waitUtil;
    }

    @Override
    public StackEntity doAssertion(TestContext testContext, StackEntity entity, CloudbreakClient cloudbreakClient) throws Exception {
        Map<String, String> statuses = waitUtil.waitAndCheckStatuses(cloudbreakClient.getCloudbreakClient(), testContext.workspaceId(), entity.getName(),
                Map.of("status", "AVAILABLE", "clusterStatus", "AVAILABLE"));
        testContext.addStatuses(statuses);
        return entity;
    }
}
