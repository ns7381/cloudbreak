package com.sequenceiq.it.cloudbreak.newway.testcase;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.sequenceiq.it.cloudbreak.newway.TestParameter;
import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.mock.MockPoolConfiguration;
import com.sequenceiq.it.config.IntegrationTestConfiguration;

@ContextConfiguration(classes = {IntegrationTestConfiguration.class, MockPoolConfiguration.class}, initializers = ConfigFileApplicationContextInitializer.class)
public abstract class AbstractIntegrationTest extends AbstractTestNGSpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIntegrationTest.class);

    @Inject
    private TestParameter testParameter;

    @Inject
    private SparkServer sparkServer;

    @BeforeSuite
    public void beforeSuite(ITestContext testngContext) {

    }

    @BeforeClass
    public void createSharedObjects()  {

    }

    @AfterClass(alwaysRun = true)
    public void cleanSharedObjects() {

    }

    @DataProvider
    public Object[][] testContext() {
        return new Object[][]{{new TestContext(testParameter), sparkServer}};
    }
}
