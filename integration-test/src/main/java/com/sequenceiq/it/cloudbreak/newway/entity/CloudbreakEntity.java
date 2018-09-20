package com.sequenceiq.it.cloudbreak.newway.entity;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.ger.StrategyV2;

public interface CloudbreakEntity {

    void create(Long workpaceId, CloudbreakClient cloudbreakClient) throws Exception;

    void setCreationStrategyV2(StrategyV2<? extends CloudbreakEntity> creationStrategyV2);
}
