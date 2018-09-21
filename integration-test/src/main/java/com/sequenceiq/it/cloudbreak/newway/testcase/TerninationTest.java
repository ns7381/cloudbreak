package com.sequenceiq.it.cloudbreak.newway.testcase;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.newway.CredentialEntity;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.action.CredentialCreateAction;
import com.sequenceiq.it.cloudbreak.newway.action.ImageCatalogCreateAction;
import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.entity.AmbariEntity;
import com.sequenceiq.it.cloudbreak.newway.entity.ClusterEntity;
import com.sequenceiq.it.cloudbreak.newway.mock.DefaultModel;
import com.sequenceiq.it.cloudbreak.newway.v3.CredentialV3Action;
import com.sequenceiq.it.cloudbreak.newway.v3.StackV3Action;

public class TerninationTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerninationTest.class);

    @BeforeMethod
    public void beforeMethod(Object[] data) {
        TestContext testContext = (TestContext) data[0];
        SparkServer sparkServer = (SparkServer) data[1];
        sparkServer.initSparkService();
        DefaultModel model = new DefaultModel();
        model.startModel(sparkServer.getSparkService(), "localhost");
        String imageCatalogAddress = sparkServer.startImageCatalog(9444);
        testContext.given();
        testContext.given(ImageCatalog.class).withUrl(imageCatalogAddress)
                .when(new ImageCatalogCreateAction())
                .when(ImageCatalog::putSetDefaultByName)
                .given(CredentialEntity.class).withParameters(Map.of("mockEndpoint", sparkServer.getEndpoint()))
                .when(new CredentialCreateAction());

    }

    @AfterMethod(alwaysRun = true)
    public void tear(Object[] data) {
        ((SparkServer) data[1]).stop();
        TestContext testContext = (TestContext) data[0];
        testContext.when(CredentialEntity.class, CredentialV3Action::deleteV2);
        testContext.when(ImageCatalog.class, ImageCatalog::deleteV2);
    }

    @Test(dataProvider = "testContext")
    public void testCreateNewRegularCluster(TestContext testContext, SparkServer sparkServer) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(Stack.class).withName(clusterName)
                .when(Stack.postV2())
                .then(Stack::waitAndCheckClusterAndStackAvailabilityStatus)
                .when(Stack.class, StackV3Action::deleteV2)
                .then(Stack::waitAndCheckClusterAndStackAvailabilityStatus);
    }
}
