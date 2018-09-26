package com.sequenceiq.it.cloudbreak.newway.context;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.it.cloudbreak.newway.ApplicationContextProvider;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.TestParameter;
import com.sequenceiq.it.cloudbreak.newway.action.ActionV2;
import com.sequenceiq.it.cloudbreak.newway.assertion.AssertionV2;
import com.sequenceiq.it.cloudbreak.newway.entity.CloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.log.Log;

public class TestContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestContext.class);

    private Map<String, CloudbreakEntity> resources = new HashMap<>();

    private Map<String, CloudbreakClient> clients = new HashMap<>();

    private Map<String, String> statuses = new HashMap<>();

    private TestParameter testParameter;

    private Map<String, Exception> exceptionMap = new HashMap<>();

    private Long workspaceId;

    public TestContext(TestParameter testParameter) {
        this.testParameter = testParameter;
    }

    public <T extends CloudbreakEntity<T>> T when(T entity, String who, ActionV2<T> action) {
        CloudbreakClient cloudbreakClient = getCloudbreakClient(who);
        try {
            return action.action(this, entity, cloudbreakClient);
        } catch (Exception e) {
            LOGGER.warn("Action is failed: {}", e.getMessage(), e);
            exceptionMap.put("when", e);
            throw new RuntimeException(e);
        }
    }

    private CloudbreakClient getCloudbreakClient(String who) {
        CloudbreakClient cloudbreakClient = clients.get(who);
        if (cloudbreakClient == null) {
            throw new IllegalStateException("Should create a client for this user: " + who);
        }
        return cloudbreakClient;
    }

    public <T extends CloudbreakEntity<T>> T when(Class<T> entityClass, ActionV2<T> action) {
        T entity = (T) resources.get(entityClass.getSimpleName());
        if (entity == null) {
            LOGGER.warn("CANNOT FOUND IN THE RESOURCES, run with the default");
            entity = init(entityClass);
        }
        return when(entity, action);
    }

    public <T extends CloudbreakEntity<T>> T when(T entity, ActionV2<T> action) {
        return when(entity, testParameter.get(CloudbreakTest.USER), action);
    }

    public <T extends CloudbreakEntity<T>> T then(T entity, String who, AssertionV2<T> assertion) {
        try {
            return assertion.doAssertion(this, entity, getCloudbreakClient(who));
        } catch (Exception e) {
            LOGGER.warn("assertion is failed: {}", e.getMessage(), e);
            exceptionMap.put("then", e);
            throw new RuntimeException(e);
        }
    }

    public <T extends CloudbreakEntity<T>> T then(T entity, AssertionV2<T> assertion) {
        return then(entity, testParameter.get(CloudbreakTest.USER), assertion);
    }

    public TestContext given() {
        CloudbreakClient cloudbreakClient = CloudbreakClient.createProxyCloudbreakClient(testParameter);
        String user = testParameter.get(CloudbreakTest.USER);
        clients.put(user, cloudbreakClient);
        workspaceId = cloudbreakClient.getCloudbreakClient()
                .workspaceV3Endpoint()
                .getByName(testParameter.get(CloudbreakTest.USER)).getId();
        return this;
    }

    public <O extends CloudbreakEntity<O>> O init(Class<O> clss) {
        Log.log("Given " + clss.getSimpleName());
        CloudbreakEntity<O> bean = ApplicationContextProvider.getBean(clss, this);
        return bean.valid();
    }

    public <O extends CloudbreakEntity<O>> O given(Class<O> clss) {
        Log.log("Given " + clss.getSimpleName());
        CloudbreakEntity<O> bean = ApplicationContextProvider.getBean(clss, this);
        resources.put(bean.getClass().getSimpleName(), bean);
        return bean.valid();
    }

    public Long workspaceId() {
        return workspaceId;
    }

    public void addStatuses(Map<String, String> statuses) {
        statuses.putAll(statuses);
    }

    public Map<String, Exception> getErrors() {
        return exceptionMap;
    }

    public <T extends CloudbreakEntity<T>> T get(Class<T> clss) {
        return (T) resources.get(clss.getSimpleName());
    }
}
