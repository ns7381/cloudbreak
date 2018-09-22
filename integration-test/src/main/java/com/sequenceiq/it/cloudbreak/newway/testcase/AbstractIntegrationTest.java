package com.sequenceiq.it.cloudbreak.newway.testcase;

import static com.sequenceiq.it.cloudbreak.newway.Mock.CLOUDBREAK_SERVER_ROOT;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.io.IOUtils;
import org.mockserver.integration.ClientAndServer;
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

import com.sequenceiq.cloudbreak.client.RestClientUtil;
import com.sequenceiq.it.cloudbreak.mock.json.CBVersion;
import com.sequenceiq.it.cloudbreak.newway.TestParameter;
import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.mock.ImageCatalogMockServerSetup;
import com.sequenceiq.it.cloudbreak.newway.mock.MockPoolConfiguration;
import com.sequenceiq.it.config.IntegrationTestConfiguration;
import com.sequenceiq.it.spark.ITResponse;

@ContextConfiguration(classes = {IntegrationTestConfiguration.class, MockPoolConfiguration.class}, initializers = ConfigFileApplicationContextInitializer.class)
public abstract class AbstractIntegrationTest extends AbstractTestNGSpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIntegrationTest.class);

    @Inject
    protected TestParameter testParameter;

    @Inject
    protected SparkServer sparkServer;

    @Inject
    protected ImageCatalogMockServerSetup imgCatalog;

    @Autowired
    protected ClientAndServer mockServer;

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


    public String getImgCatalogUrl(){
        return String.join("", "https://localhost", ":", mockServer.getLocalPort() + "", ITResponse.IMAGE_CATALOG);
    }



    public String patchCbVersion(String imgCatalog) {
        Client client = RestClientUtil.get();
        WebTarget target = client.target(testParameter.get(CLOUDBREAK_SERVER_ROOT) + "/info");
        CBVersion cbVersion = target.request().get().readEntity(CBVersion.class);
        String version = cbVersion.getApp().getVersion();
        return imgCatalog.replace("CB_VERSION", cbVersion.getApp().getVersion());
    }

    public static String responseFromJsonFile(String path) {
        try (InputStream inputStream = ITResponse.class.getResourceAsStream("/mockresponse/" + path)) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            return "";
        }
    }
}
