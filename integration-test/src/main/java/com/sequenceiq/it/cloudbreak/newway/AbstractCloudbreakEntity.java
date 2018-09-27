package com.sequenceiq.it.cloudbreak.newway;

import static com.sequenceiq.it.cloudbreak.newway.finder.Finders.same;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.sequenceiq.it.cloudbreak.newway.action.ActionV2;
import com.sequenceiq.it.cloudbreak.newway.assertion.AssertionV2;
import com.sequenceiq.it.cloudbreak.newway.cloud.v2.MockCloudProvider;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.entity.CloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.finder.Attribute;
import com.sequenceiq.it.cloudbreak.newway.finder.Finder;

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
        return when(entityClass, action, true);
    }

    public T when(Class<T> entityClass, ActionV2<T> action, boolean skipOnFail) {
        return testContext.when(entityClass, action, skipOnFail);
    }

    public T when(String who, ActionV2<T> action) {
        return when(who, action, true);
    }

    public T when(String who, ActionV2<T> action, boolean skipOnFail) {
        return testContext.when((T) this, who, action, skipOnFail);
    }

    public T when(ActionV2<T> action) {
        return when(action, true);
    }

    public T when(ActionV2<T> action, boolean skipOnFail) {
        return testContext.when((T) this, action, skipOnFail);
    }

    public T then(AssertionV2<T> assertion) {
        return then(assertion, true);
    }

    public T then(AssertionV2<T> assertion, boolean skipOnFail) {
        return testContext.then((T) this, assertion, skipOnFail);
    }

    public <O extends CloudbreakEntity<O>> O given(Class<O> clss) {
        return testContext.given(clss);
    }

    public <O> T select(Attribute<T, O> attribute) {
        return select(null, attribute);
    }

    public <O> T select(String key, Attribute<T, O> attribute) {
        return select(key, attribute, same());
    }

    public <O> T select(String key, Attribute<T, O> attribute, Finder<O> finder) {
        return select(key, attribute, finder, true);
    }

    public <O> T select(String key, Attribute<T, O> attribute, Finder<O> finder, boolean skipOnFail) {
        return testContext.select((T) this, key, attribute, finder, skipOnFail);
    }

    public <O> T select(Attribute<T, O> attribute, Finder<O> finder) {
        return select(attribute, finder, true);
    }

    public <O> T select(Attribute<T, O> attribute, Finder<O> finder, boolean skipOnFail) {
        return testContext.select((T) this, attribute, finder, skipOnFail);
    }

    public <O> T capture(String key, Attribute<T, O> attribute) {
        return capture(key, attribute, true);
    }

    public <O> T capture(String key, Attribute<T, O> attribute, boolean skipOnFail) {
        return testContext.capture((T) this, key, attribute, skipOnFail);
    }

    public <O> T capture(Attribute<T, O> attribute) {
        return capture(attribute, true);
    }

    public <O> T capture(Attribute<T, O> attribute, boolean skipOnFail) {
        return testContext.capture((T) this, null, attribute, skipOnFail);
    }

    public <O> T verify(String key, Attribute<T, O> attribute) {
        return verify(key, attribute, true);
    }

    public <O> T verify(String key, Attribute<T, O> attribute, boolean skipOnFail) {
        return testContext.verify((T) this, key, attribute, skipOnFail);
    }

    public <O> T verify(Attribute<T, O> attribute) {
        return verify(attribute, true);
    }

    public <O> T verify(Attribute<T, O> attribute, boolean skipOnFail) {
        return testContext.verify((T) this, null, attribute, skipOnFail);
    }

    public T await(Map<String, String> statuses) {
        return testContext.await((T) this, statuses, true);
    }

    public T await(Map<String, String> statuses, boolean skipOnFail) {
        return testContext.await((T) this, statuses, skipOnFail);
    }
}
