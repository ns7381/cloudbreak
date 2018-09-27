package com.sequenceiq.it.cloudbreak.newway.testcase;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.sequenceiq.it.cloudbreak.newway.TestParameter;
import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.mock.ImageCatalogMockServerSetup;
import com.sequenceiq.it.cloudbreak.newway.mock.MockPoolConfiguration;
import com.sequenceiq.it.cloudbreak.newway.wait.WaitUtil;
import com.sequenceiq.it.config.IntegrationTestConfiguration;

@ContextConfiguration(classes = {IntegrationTestConfiguration.class, MockPoolConfiguration.class}, initializers = ConfigFileApplicationContextInitializer.class)
public abstract class AbstractIntegrationTest extends AbstractTestNGSpringContextTests {

    protected static final Map<String, String> STACK_AVAILABLE = Map.of("status", "AVAILABLE", "clusterStatus", "AVAILABLE");

    protected static final Map<String, String> STACK_DELETED = Map.of("status", "DELETE_COMPLETED");

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIntegrationTest.class);

    @Inject
    private TestParameter testParameter;

    @Inject
    private SparkServer sparkServer;

    @Inject
    private ImageCatalogMockServerSetup imgCatalog;

    @Autowired
    private WireMockServer mockServer;

    @Inject
    protected WaitUtil waitUtil;

    @BeforeSuite
    public void beforeSuite(ITestContext testngContext) {

    }

    @BeforeClass
    public void createSharedObjects() {

    }

    @AfterClass(alwaysRun = true)
    public void cleanSharedObjects() {

    }

    public TestParameter getTestParameter() {
        return testParameter;
    }

    public SparkServer getSparkServer() {
        return sparkServer;
    }

    public ImageCatalogMockServerSetup getImgCatalog() {
        return imgCatalog;
    }

    public WireMockServer getMockServer() {
        return mockServer;
    }

    @DataProvider
    public Object[][] testContext() {
        return new Object[][]{{new TestContext(testParameter, waitUtil), sparkServer}};
    }
}
