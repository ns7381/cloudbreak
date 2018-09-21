package com.sequenceiq.it.cloudbreak.newway;

import java.util.Set;

import javax.inject.Inject;

import com.sequenceiq.it.cloudbreak.newway.action.ActionV2;
import com.sequenceiq.it.cloudbreak.newway.assertion.AssertionV2;
import com.sequenceiq.it.cloudbreak.newway.cloud.v2.MockCloudProvider;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.entity.CloudbreakEntity;

public abstract class AbstractCloudbreakEntity<R, S, T extends CloudbreakEntity<T>> extends Entity implements CloudbreakEntity<T> {

    @Inject
    private TestParameter testParameter;

    @Inject
    private MockCloudProvider cloudProvider;

    private String name;

    private R request;

    private S response;

    private Set<S> responses;

    private TestContext testContext;

    protected AbstractCloudbreakEntity(String newId) {
        super(newId);
    }

    protected AbstractCloudbreakEntity(R request, TestContext testContext) {
        this.request = request;
        this.testContext = testContext;
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

    protected TestContext getTestContext() {
        return testContext;
    }

    public T valid() {
        return null;
    }

    public Long workspaceId() {
        return testContext.workspaceId();
    }

    public T when(Class<T> entityClass, ActionV2<T> action) {
        return testContext.when(entityClass, action);
    }

    public T when(String who, ActionV2<T> action) {
        return testContext.when((T) this, who, action);
    }

    public T when(ActionV2<T> action) {
        return testContext.when((T) this, action);
    }

    public T then(AssertionV2<T> assertion) {
        return testContext.then((T) this, assertion);
    }

    public <O extends CloudbreakEntity<O>> O given(Class<O> clss) {
        return testContext.given(clss);
    }
}
