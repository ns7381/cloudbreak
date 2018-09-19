package com.sequenceiq.it.cloudbreak.newway;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.newway.ger.StrategyV2;
import com.sequenceiq.it.cloudbreak.newway.log.Log;
import com.sequenceiq.it.config.IntegrationTestConfiguration;

@ContextConfiguration(classes = IntegrationTestConfiguration.class, initializers = ConfigFileApplicationContextInitializer.class)
public class GherkinTest extends AbstractTestNGSpringContextTests {
    public static final String RESULT = "RESULT";

    public static final String EMPTY_MESSAGE = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(GherkinTest.class);

    @Inject
    private IntegrationTestContext itContext;

    @Inject
    private CloudbreakClient cloudbreakClient;

    protected IntegrationTestContext getItContext() {
        return itContext;
    }

    protected <T> T given(Entity entity, String message) throws Exception {
        return given(entity, null, message);
    }

    protected <T> T given(Entity entity, StrategyV2 strategy, String message) throws Exception {
        if (entity != null) {
            Log.log("Given " + message);
            entity.setCreationStrategyV2(strategy);
            entity.create(itContext, cloudbreakClient);
            itContext.putContextParam(entity.getEntityId(), entity);
        }
        return (T) entity;
    }

    protected void given(Entity entity) throws Exception {
        if (entity != null) {
            given(entity, null, entity.getEntityId());
        }
    }

    protected void when(Action<?> action, String message) throws Exception {
        Log.log("When " + message);
        itContext.putContextParam(RESULT, action.action(itContext));
    }

    protected void when(Action<?> action) throws Exception {
        when(action, EMPTY_MESSAGE);
    }

    protected void then(Assertion<?> assertion, String message) {
        Log.log("Then " + message);
        assertion.doAssertion(itContext);
    }

    protected void then(Assertion<?> assertion) {
        then(assertion, EMPTY_MESSAGE);
    }
}
