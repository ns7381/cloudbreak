package com.sequenceiq.it.cloudbreak.newway.ger;

import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.Entity;
import com.sequenceiq.it.cloudbreak.newway.Strategy;

public interface StrategyV2 extends Strategy {
    void doAction(IntegrationTestContext integrationTestContext, Entity entity, CloudbreakClient client) throws Exception;
}
