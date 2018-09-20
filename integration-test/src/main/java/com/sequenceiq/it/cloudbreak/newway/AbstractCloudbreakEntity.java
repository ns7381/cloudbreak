package com.sequenceiq.it.cloudbreak.newway;

import java.util.Set;

import javax.inject.Inject;

import com.sequenceiq.it.cloudbreak.newway.cloud.v2.MockCloudProvider;
import com.sequenceiq.it.cloudbreak.newway.entity.CloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.ger.StrategyV2;

public abstract class AbstractCloudbreakEntity<R, S> extends Entity implements CloudbreakEntity {

    @Inject
    private TestParameter testParameter;

    @Inject
    private MockCloudProvider cloudProvider;

    private String name;

    private R request;

    private S response;

    private Set<S> responses;

    private StrategyV2<CloudbreakEntity> creationStrategyV2;

    protected AbstractCloudbreakEntity(String newId) {
        super(newId);
    }

    public Set<S> getResponses() {
        return responses;
    }

    public void setResponses(Set<S> responses) {
        this.responses = responses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public R getRequest() {
        return request;
    }

    public void setRequest(R request) {
        this.request = request;
    }

    public void setResponse(S response) {
        this.response = response;
    }

    public S getResponse() {
        return response;
    }

    protected TestParameter getTestParameter() {
        return testParameter;
    }

    protected MockCloudProvider getCloudProvider() {
        return cloudProvider;
    }

    public void setCreationStrategyV2(StrategyV2<? extends CloudbreakEntity> creationStrategyV2) {
        this.creationStrategyV2 = (StrategyV2<CloudbreakEntity>) creationStrategyV2;
    }

    public void create(Long workpaceId, CloudbreakClient cloudbreakClient) throws Exception {
        if (creationStrategyV2 != null) {
            creationStrategyV2.doAction(workpaceId, this, cloudbreakClient);
        }
    }
}
