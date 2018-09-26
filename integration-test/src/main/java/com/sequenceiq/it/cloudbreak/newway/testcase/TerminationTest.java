package com.sequenceiq.it.cloudbreak.newway.testcase;


import static com.sequenceiq.it.cloudbreak.newway.mock.model.AmbariMock.CLUSTERS_CLUSTER_REQUESTS_REQUEST;
import static com.sequenceiq.it.spark.ITResponse.AMBARI_API_ROOT;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.newway.CredentialEntity;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.action.CredentialCreateAction;
import com.sequenceiq.it.cloudbreak.newway.action.ImageCatalogCreateIfNotExistsAction;
import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.entity.AmbariEntity;
import com.sequenceiq.it.cloudbreak.newway.entity.ClusterEntity;
import com.sequenceiq.it.cloudbreak.newway.mock.DefaultModel;
import com.sequenceiq.it.cloudbreak.newway.v3.CredentialV3Action;
import com.sequenceiq.it.cloudbreak.newway.v3.StackV3Action;

import spark.Route;

public class TerminationTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminationTest.class);

    @BeforeMethod
    public void beforeMethod(Object[] data) {
        TestContext testContext = (TestContext) data[0];
        SparkServer sparkServer = (SparkServer) data[1];
        sparkServer.initSparkService();
        getImgCatalog().configureImgCatalogMock(getTestParameter());
        DefaultModel model = new DefaultModel();
        model.startModel(sparkServer.getSparkService(), "localhost");

        modifySparkMock(model);
        testContext.given();
        testContext.given(ImageCatalog.class).withUrl(getImgCatalog().getImgCatalogUrl())
                .when(new ImageCatalogCreateIfNotExistsAction())
                .when(ImageCatalog::putSetDefaultByName)
                .given(CredentialEntity.class).withParameters(Map.of("mockEndpoint", sparkServer.getEndpoint()))
                .when(new CredentialCreateAction());

    }

    private void modifySparkMock(DefaultModel model) {
//        StatefulRoute customResponse = (request, response, defaultModel) -> {
//            response.status(404);
//            response.body("{}");
//            return response;
//        };

        Route customResponse2 = (request, response) -> {
            response.status(404);
            return response;
        };

        model.getAmbariMock().getDynamicRouteStack().overrideResponseByUrlWithSimple(HttpMethod.GET, AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
        model.getAmbariMock().getDynamicRouteStack().overrideResponseByUrlWithSimple(HttpMethod.GET, AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
        model.getAmbariMock().getDynamicRouteStack().overrideResponseByUrlWithSimple(HttpMethod.GET, AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
    }

    @AfterMethod(alwaysRun = true)
    public void tear(Object[] data) {
        ((SparkServer) data[1]).stop();
        TestContext testContext = (TestContext) data[0];
        try {
            testContext.when(CredentialEntity.class, CredentialV3Action::deleteV2);
        } catch (Exception e) {

        }
        try {
            testContext.when(ImageCatalog.class, ImageCatalog::deleteV2);
        } catch (Exception e) {

        }
    }

    @Test(dataProvider = "testContext")
    public void testCreateNewRegularCluster(TestContext testContext, SparkServer sparkServer) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(Stack.class).withName(clusterName).withGatewayPort(sparkServer.getPort())
                .when(Stack.postV2())
                .then(Stack::waitAndCheckClusterAndStackAvailabilityStatus)
                .when(Stack.class, StackV3Action::deleteV2)
                .then(Stack::waitAndCheckClusterDeleted);
    }
}
