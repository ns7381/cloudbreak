package com.sequenceiq.it.cloudbreak.newway.ger;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.entity.CloudbreakEntity;

public interface StrategyV2<T extends CloudbreakEntity> {
    void doAction(Long workspaceId, T entity, CloudbreakClient client) throws Exception;
}
