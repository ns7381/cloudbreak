package com.sequenceiq.it.cloudbreak.newway.action;

import java.util.Collections;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;

public class StackRefreshAction implements ActionV2<StackEntity> {

    @Override
    public StackEntity action(TestContext testContext, StackEntity entity, CloudbreakClient cloudbreakClient) throws Exception {
        entity.setResponse(
                cloudbreakClient.getCloudbreakClient().stackV3Endpoint().getByNameInWorkspace(testContext.workspaceId(), entity.getName(), Collections.emptySet())
        );
        return entity;
    }
}
