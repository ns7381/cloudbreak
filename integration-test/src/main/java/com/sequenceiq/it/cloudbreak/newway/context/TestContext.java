package com.sequenceiq.it.cloudbreak.newway.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.sequenceiq.it.cloudbreak.newway.ApplicationContextProvider;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.TestParameter;
import com.sequenceiq.it.cloudbreak.newway.action.ActionV2;
import com.sequenceiq.it.cloudbreak.newway.assertion.AssertionV2;
import com.sequenceiq.it.cloudbreak.newway.entity.CloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.finder.Attribute;
import com.sequenceiq.it.cloudbreak.newway.finder.Capture;
import com.sequenceiq.it.cloudbreak.newway.finder.Finder;
import com.sequenceiq.it.cloudbreak.newway.log.Log;
import com.sequenceiq.it.cloudbreak.newway.wait.WaitUtil;

public class TestContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestContext.class);

    private Map<String, CloudbreakEntity<?>> resources = new HashMap<>();

    private Map<String, CloudbreakClient> clients = new HashMap<>();

    private Map<String, String> statuses = new HashMap<>();

    private TestParameter testParameter;

    private Map<String, Exception> exceptionMap = new HashMap<>();

    private Map<String, Object> selections = new HashMap<>();

    private Map<String, Capture> captures = new HashMap<>();

    private Long workspaceId;

    private WaitUtil waitUtil;

    public TestContext(TestParameter testParameter, WaitUtil waitUtil) {
        this.testParameter = testParameter;
        this.waitUtil = waitUtil;
    }

    public <T extends CloudbreakEntity<T>> T when(T entity, String who, ActionV2<T> action) {
        return when(entity, who, action, true);
    }

    public <T extends CloudbreakEntity<T>> T when(T entity, String who, ActionV2<T> action, boolean skipOnFail) {
        if (!exceptionMap.isEmpty() && skipOnFail) {
            LOGGER.info("Should be skipped beacause of previous error. when [{}]", action);
            return entity;
        }
        LOGGER.info("when {} on {}", action, entity);
        CloudbreakClient cloudbreakClient = getCloudbreakClient(who);
        try {
            return action.action(this, entity, cloudbreakClient);
        } catch (Exception e) {
            LOGGER.error("when [{}] is failed: {}", action, e.getMessage(), e);
            exceptionMap.put("when", e);
        }
        return entity;
    }

    public <T extends CloudbreakEntity<T>> T when(Class<T> entityClass, ActionV2<T> action) {
        return when(entityClass, action, true);
    }

    public <T extends CloudbreakEntity<T>> T when(Class<T> entityClass, ActionV2<T> action, boolean skipOnFail) {
        T entity = (T) resources.get(entityClass.getSimpleName());
        if (entity == null) {
            LOGGER.warn("Cannot found in the resources [{}], run with the default", entityClass.getSimpleName());
            entity = init(entityClass);
        }
        return when(entity, action, skipOnFail);
    }

    public <T extends CloudbreakEntity<T>> T when(T entity, ActionV2<T> action) {
        return when(entity, action, true);
    }

    public <T extends CloudbreakEntity<T>> T when(T entity, ActionV2<T> action, boolean skipOnFail) {
        return when(entity, getDefaultUser(), action, skipOnFail);
    }

    public <T extends CloudbreakEntity<T>> T then(T entity, String who, AssertionV2<T> assertion) {
        return then(entity, who, assertion, true);
    }

    public <T extends CloudbreakEntity<T>> T then(T entity, String who, AssertionV2<T> assertion, boolean skipOnFail) {
        if (!exceptionMap.isEmpty() && skipOnFail) {
            LOGGER.info("Should be skipped beacause of previous error. when [{}]", assertion);
            return entity;
        }
        LOGGER.info("then {} on {}", assertion, entity);
        try {
            return assertion.doAssertion(this, entity, getCloudbreakClient(who));
        } catch (Exception e) {
            LOGGER.error("then [{}] is failed: {}", assertion, e.getMessage());
            exceptionMap.put("then " + assertion, e);
        }
        return entity;
    }

    public <T extends CloudbreakEntity<T>> T then(T entity, AssertionV2<T> assertion) {
        return then(entity, assertion, true);
    }

    public <T extends CloudbreakEntity<T>> T then(T entity, AssertionV2<T> assertion, boolean skipOnFail) {
        return then(entity, getDefaultUser(), assertion, skipOnFail);
    }

    public TestContext given() {
        CloudbreakClient cloudbreakClient = CloudbreakClient.createProxyCloudbreakClient(testParameter);
        String user = getDefaultUser();
        clients.put(user, cloudbreakClient);
        workspaceId = cloudbreakClient.getCloudbreakClient()
                .workspaceV3Endpoint()
                .getByName(getDefaultUser()).getId();
        return this;
    }

    private String getDefaultUser() {
        return testParameter.get(CloudbreakTest.USER);
    }

    public <O extends CloudbreakEntity<O>> O init(Class<O> clss) {
        Log.log(LOGGER, "Given " + clss.getSimpleName());
        CloudbreakEntity<O> bean = ApplicationContextProvider.getBean(clss, this);
        return bean.valid();
    }

    public <O extends CloudbreakEntity<O>> O given(Class<O> clss) {
        O entity = (O) resources.computeIfAbsent(clss.getSimpleName(), (value) -> init(clss));
        resources.put(clss.getSimpleName(), entity);
        return entity;
    }

    public Long workspaceId() {
        return workspaceId;
    }

    public void addStatuses(Map<String, String> statuses) {
        this.statuses.putAll(statuses);
    }

    public Map<String, String> getStatuses() {
        return statuses;
    }

    public Map<String, Exception> getErrors() {
        return exceptionMap;
    }

    public <T extends CloudbreakEntity<T>> T get(Class<T> clss) {
        return (T) resources.get(clss.getSimpleName());
    }

    public <O> O getSelected(String key) {
        return (O) selections.get(key);
    }

    public <O, T extends CloudbreakEntity<T>> T select(T entity, Attribute<T, O> attribute, Finder<O> finder, boolean skipOnFail) {
        return select(entity, null, attribute, finder, skipOnFail);
    }

    public <O, T extends CloudbreakEntity<T>> T select(T entity, String key, Attribute<T, O> attribute, Finder<O> finder, boolean skipOnFail) {
        if (!exceptionMap.isEmpty() && skipOnFail) {
            LOGGER.info("Should be skipped beacause of previous error. select: attr: [{}], finder: [{}]", attribute, finder);
            return entity;
        }
        LOGGER.info("try to select (attribute: [{}], finder: [{}])", attribute, finder);
        try {
            O attr = attribute.get(entity);
            Optional<O> o = Optional.ofNullable(finder.find(attr));
            o.ifPresentOrElse(ob -> {
                String selectedKey = key;
                if (StringUtils.isEmpty(key)) {
                    selectedKey = ob.getClass().getSimpleName();
                }
                selections.put(selectedKey, ob);
                LOGGER.info("Selected object: {}", ob);
            }, () -> LOGGER.warn("Cannot find the Object"));
        } catch (Exception e) {
            LOGGER.error("select (attribute: [{}], finder: [{}]) is failed: {}", attribute, finder, e.getMessage());
            exceptionMap.put("select " + attribute + ", finder " + finder, e);
        }
        return entity;
    }

    public <O, T extends CloudbreakEntity<T>> T capture(T entity, String key, Attribute<T, O> attribute, boolean skipOnFail) {
        if (!exceptionMap.isEmpty() && skipOnFail) {
            LOGGER.info("Should be skipped beacause of previous error. capture [{}]", attribute);
            return entity;
        }
        LOGGER.info("try to capture (key={}) [{}]", key, attribute);
        try {
            O attr = attribute.get(entity);
            String captureKey = key;
            if (StringUtils.isEmpty(key)) {
                captureKey = entity.getClass().getSimpleName();
            }
            captures.put(captureKey, new Capture(attr));
        } catch (Exception e) {
            LOGGER.error("capture [{}] is failed: {}", attribute, e.getMessage());
            exceptionMap.put("capture " + attribute, e);
        }
        return entity;
    }

    public <O, T extends CloudbreakEntity<T>> T verify(T entity, String key, Attribute<T, O> attribute, boolean skipOnFail) {
        if (!exceptionMap.isEmpty() && skipOnFail) {
            LOGGER.info("Should be skipped beacause of previous error. verify [{}]", attribute);
            return entity;
        }
        LOGGER.info("try to verify (key={}). attribute [{}]", key, attribute);
        try {
            O attr = attribute.get(entity);
            String captureKey = key;
            if (StringUtils.isEmpty(key)) {
                captureKey = entity.getClass().getSimpleName();
            }
            Capture capture = captures.get(captureKey);
            if (capture == null) {
                throw new RuntimeException(String.format("The key [%s] is invalid capture is not verified", captureKey));
            } else {
                capture.verify(attr);
            }
        } catch (Exception e) {
            LOGGER.error("verify [{}] is failed: {}", attribute, e.getMessage());
            exceptionMap.put("verify " + attribute, e);
        }
        return entity;
    }

    private CloudbreakClient getCloudbreakClient(String who) {
        CloudbreakClient cloudbreakClient = clients.get(who);
        if (cloudbreakClient == null) {
            throw new IllegalStateException("Should create a client for this user: " + who);
        }
        return cloudbreakClient;
    }


    public <T extends CloudbreakEntity<T>> T await(T entity, Map<String, String> desiredStatuses, boolean skipOnFail) {
        return await(getDefaultUser(), entity, desiredStatuses, skipOnFail);
    }

    public <T extends CloudbreakEntity<T>> T await(String who, T entity, Map<String, String> desiredStatuses, boolean skipOnFail) {
        if (!exceptionMap.isEmpty() && skipOnFail) {
            LOGGER.info("Should be skipped beacause of previous error. await [{}]", desiredStatuses);
            return entity;
        }
        try {
            statuses.putAll(waitUtil.waitAndCheckStatuses(getCloudbreakClient(who).getCloudbreakClient(), workspaceId, entity.getName(), desiredStatuses));
        } catch (Exception e) {
            LOGGER.error("await [{}] is failed for statuses {}: {}", entity, desiredStatuses, e.getMessage());
            exceptionMap.put("await " + entity + " for desired statuses" + desiredStatuses, e);
        }
        return entity;
    }
}
