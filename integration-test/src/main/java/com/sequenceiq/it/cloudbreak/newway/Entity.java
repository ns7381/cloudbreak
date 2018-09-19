package com.sequenceiq.it.cloudbreak.newway;

import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.newway.ger.StrategyV2;

public abstract class Entity {
    private final String entityId;

    private Strategy creationStrategy;

    private StrategyV2 creationStrategyV2;

    protected Entity(String id) {
        entityId = id;
    }

    String getEntityId() {
        return entityId;
    }

    public void setCreationStrategy(Strategy strategy) {
        creationStrategy = strategy;
    }

    public void setCreationStrategyV2(StrategyV2 creationStrategyV2) {
        this.creationStrategyV2 = creationStrategyV2;
    }

    void create(IntegrationTestContext integrationTestContext) throws Exception {
        create(integrationTestContext, null);
    }
    public void create(IntegrationTestContext integrationTestContext, CloudbreakClient cloudbreakClient) throws Exception {
        if (creationStrategy != null) {
            creationStrategy.doAction(integrationTestContext, this);
        }
        if (creationStrategyV2 != null) {
            cloudbreakClient.create(integrationTestContext);
            creationStrategyV2.doAction(integrationTestContext, this, cloudbreakClient);
        }
    }
}
