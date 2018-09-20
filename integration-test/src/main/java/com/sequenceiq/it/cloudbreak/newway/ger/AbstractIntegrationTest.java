package com.sequenceiq.it.cloudbreak.newway.ger;

import static com.sequenceiq.it.cloudbreak.newway.CloudbreakTest.WORKSPACE_ID;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.Credential;
import com.sequenceiq.it.cloudbreak.newway.GherkinTest;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.mock.DefaultModel;
import com.sequenceiq.it.cloudbreak.newway.v3.CredentialV3Action;
import com.sequenceiq.it.cloudbreak.newway.v3.ImageCatalogV3Action;

public abstract class AbstractIntegrationTest extends GherkinTest {

    @Inject
    private CloudbreakClient cloudbreakClient;

    @Inject
    private ApplicationContext applicationContext;

    private SparkServer sparkServer;

    @BeforeSuite
    public void beforeSuite() {

    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        sparkServer = applicationContext.getBean(SparkServer.class, "localhost", 9444);
        sparkServer.initSparkService();
        DefaultModel model = new DefaultModel();
        model.startModel(sparkServer.getSparkService(), "localhost");
        String imageCatalogAddress = sparkServer.startImageCatalog(9444);
        given(ImageCatalog.valid().withUrl(imageCatalogAddress), new ImageCatalogCreateStrategy(), "an image catalog");
        ImageCatalogV3Action.putSetDefaultByName(getItContext(), ImageCatalog.valid(), cloudbreakClient);
        given(Credential.valid().withParameters(Map.of("mockEndpoint", sparkServer.getEndpoint())), new CredentialCreateStrategy(), "a credential");
    }

    @BeforeClass
    public void createSharedObjects() throws Exception {
        getItContext().putContextParam(WORKSPACE_ID, cloudbreakClient.getCloudbreakClient()
                .workspaceV3Endpoint()
                .getByName(getItContext().getContextParam(CloudbreakTest.USER)).getId());
    }

    @AfterClass(alwaysRun = true)
    public void cleanSharedObjects() {
        ImageCatalogV3Action.delete(getItContext(), ImageCatalog.valid(), cloudbreakClient);
        CredentialV3Action.delete(getItContext(), Credential.valid(), cloudbreakClient);
    }

    @AfterMethod
    public void tear() {
        sparkServer.stop();
    }

    public CloudbreakClient getCloudbreakClient() {
        return cloudbreakClient;
    }

    public SparkServer getSparkServer() {
        return sparkServer;
    }
}
