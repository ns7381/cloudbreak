package com.sequenceiq.it.cloudbreak.newway.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;

public class StackDeleteInstanceAction implements ActionV2<StackEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackDeleteInstanceAction.class);

    @Override
    public StackEntity action(TestContext testContext, StackEntity entity, CloudbreakClient cloudbreakClient) throws Exception {
        LOGGER.info("try to delete stack {}", entity.getName());
        cloudbreakClient.getCloudbreakClient().stackV3Endpoint().deleteInstance(testContext.workspaceId(), entity.getName(), "", true);
        return entity;
    }
}
