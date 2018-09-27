package com.sequenceiq.it.cloudbreak.newway.testcase;


import static com.sequenceiq.it.cloudbreak.newway.Mock.gson;
import static com.sequenceiq.it.cloudbreak.newway.mock.ImageCatalogMockServerSetup.responseFromJsonFile;
import static com.sequenceiq.it.cloudbreak.newway.mock.model.AmbariMock.CLUSTERS_CLUSTER_REQUESTS_REQUEST;
import static com.sequenceiq.it.spark.ITResponse.AMBARI_API_ROOT;

import java.util.Map;

import javax.inject.Inject;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sequenceiq.cloudbreak.cloud.model.CloudVmMetaDataStatus;
import com.sequenceiq.it.cloudbreak.newway.CredentialEntity;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.action.CredentialCreateAction;
import com.sequenceiq.it.cloudbreak.newway.action.ImageCatalogCreateIfNotExistsAction;
import com.sequenceiq.it.cloudbreak.newway.action.StackRefreshAction;
import com.sequenceiq.it.cloudbreak.newway.action.WaitAndCheckClusterAndStackAvailablityAction;
import com.sequenceiq.it.cloudbreak.newway.action.WaitAndCheckClusterDeletedAction;
import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.entity.AmbariEntity;
import com.sequenceiq.it.cloudbreak.newway.entity.ClusterEntity;
import com.sequenceiq.it.cloudbreak.newway.mock.DefaultModel;
import com.sequenceiq.it.cloudbreak.newway.v3.CredentialV3Action;
import com.sequenceiq.it.cloudbreak.newway.v3.StackV3Action;
import com.sequenceiq.it.cloudbreak.newway.wait.WaitUtil;
import com.sequenceiq.it.spark.ambari.AmbariClusterResponse;
import com.sequenceiq.it.spark.ambari.AmbariComponentStatusOnHostResponse;
import com.sequenceiq.it.spark.ambari.AmbariServiceConfigResponse;
import com.sequenceiq.it.spark.ambari.EmptyAmbariClusterResponse;
import com.sequenceiq.it.spark.ambari.EmptyAmbariResponse;
import com.sequenceiq.it.spark.ambari.v2.AmbariClustersHostsResponse;
import com.sequenceiq.it.spark.ambari.v2.AmbariHostComponentStateResponse;
import com.sequenceiq.it.spark.ambari.v2.AmbariHostResponse;
import com.sequenceiq.it.spark.ambari.v2.AmbariRequestIdRespone;
import com.sequenceiq.it.spark.ambari.v2.AmbariRequestStatusResponse;
import com.sequenceiq.it.spark.ambari.v2.AmbariStrRequestIdRespone;

import spark.Route;
import spark.Service;

public class TerminationTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminationTest.class);

    @Inject
    private WaitUtil waitUtil;

    DefaultModel model;

    @BeforeMethod
    public void beforeMethod(Object[] data) {
        TestContext testContext = (TestContext) data[0];
        SparkServer sparkServer = (SparkServer) data[1];
        sparkServer.initSparkService();
        getImgCatalog().configureImgCatalogMock(getTestParameter());
        model = new DefaultModel();
        model.startModel(sparkServer.getSparkService(), "localhost");

//        modifySparkMock(model);
        testContext.given();
        testContext.given(ImageCatalog.class).withUrl(getImgCatalog().getImgCatalogUrl())
                .when(new ImageCatalogCreateIfNotExistsAction())
                .when(ImageCatalog::putSetDefaultByName)
                .given(CredentialEntity.class)
                .withParameters(Map.of("mockEndpoint", sparkServer.getEndpoint()))
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

        model.getAmbariMock().getDynamicRouteStack().get(AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
        model.getAmbariMock().getDynamicRouteStack().get(AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
        model.getAmbariMock().getDynamicRouteStack().get(AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
    }

    @AfterMethod(alwaysRun = true)
    public void tear(Object[] data) {
        ((SparkServer) data[1]).stop();
        TestContext testContext = (TestContext) data[0];
        testContext.when(CredentialEntity.class, CredentialV3Action::deleteV2, false);
        testContext.when(ImageCatalog.class, ImageCatalog::deleteV2, false);

        if (!testContext.getErrors().isEmpty()) {
            testContext.getErrors().forEach(LOGGER::error);
            Assert.fail("See exceptions below");
        }
    }

    @Test(dataProvider = "testContext")
    public void testCreateNewRegularCluster(TestContext testContext, SparkServer sparkServer) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        model.setClusterName(clusterName);
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(sparkServer.getPort())
                .when(Stack.postV2())
                .then(new WaitAndCheckClusterAndStackAvailablityAction(waitUtil))
                .when(StackEntity.class, StackV3Action::deleteV2, false)
                .then(WaitAndCheckClusterDeletedAction.create());
    }

    @Test(dataProvider = "testContext")
    public void testInstanceTermination(TestContext testContext, SparkServer sparkServer) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        addMappings(clusterName, sparkServer);
        testContext
                // create stack
                .given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(sparkServer.getPort())
                .when(Stack.postV2())
                .await(STACK_AVAILABLE)
                .when(new StackRefreshAction())
                //select an instance id
                .select("instanceId", s -> s.getInstanceId("worker"))
                .capture("metadatasize", s -> s.getInstanceMetaData("worker").size() - 1)
                .when(Stack::deleteInstance)
                .await(STACK_AVAILABLE)
                .when(new StackRefreshAction())
                .verify("metadatasize", s -> s.getInstanceMetaData("worker").size())
                //cleanup
                .when(StackEntity.class, StackV3Action::deleteV2, false)
                .await(STACK_DELETED, false);
    }

    @Test(dataProvider = "testContext")
    @Ignore
    public void testInstanceTermination2(TestContext testContext, SparkServer sparkServer) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(sparkServer.getPort())
                .when(Stack.postV2())
                .then(new WaitAndCheckClusterAndStackAvailablityAction(waitUtil));

        String hostGroupName = "worker";
        StackEntity stack = testContext.get(StackEntity.class);
        testContext.when(stack, new StackRefreshAction());
        int before = stack.getInstanceMetaData(hostGroupName).size();

        stack.when(Stack.deleteInstance(stack.getInstanceId(hostGroupName)))
                .then(new WaitAndCheckClusterAndStackAvailablityAction(waitUtil));

        stack.when(new StackRefreshAction());
        int after = stack.getInstanceMetaData(hostGroupName).size();

        stack.when(StackEntity.class, StackV3Action::deleteV2, false)
                .then(WaitAndCheckClusterDeletedAction.create(), false);

        Assert.assertEquals(after, before - 1);
    }


    private void addMappings(String clusterName, SparkServer sparkServer) {
        Map<String, CloudVmMetaDataStatus> instanceMap = model.getInstanceMap();
        Service sparkService = sparkServer.getSparkService();

        int requestId = 100;
        sparkService.delete(AMBARI_API_ROOT + "/clusters/:cluster/hosts/:hostname", new EmptyAmbariResponse());
        sparkService.delete(AMBARI_API_ROOT + "/clusters/:cluster/hosts/:hostname/host_components/*", new EmptyAmbariResponse());
        sparkService.get(AMBARI_API_ROOT + "/clusters/:cluster/hosts/:hostname/host_components/*", new AmbariComponentStatusOnHostResponse());
        sparkService.post(AMBARI_API_ROOT + "/clusters/" + clusterName + "/requests", new AmbariRequestIdRespone(requestId));
        sparkService.get(AMBARI_API_ROOT + "/clusters/" + clusterName + "/requests/100", new AmbariRequestStatusResponse(requestId, 100));
        sparkService.put(AMBARI_API_ROOT + "/clusters/" + clusterName + "/requests/" + requestId, new EmptyAmbariClusterResponse());
        sparkService.put(AMBARI_API_ROOT + "/clusters/" + clusterName + "/host_components", new AmbariStrRequestIdRespone(requestId));
        sparkService.get(AMBARI_API_ROOT + "/clusters/:cluster/hosts", new AmbariHostComponentStateResponse(instanceMap));
        sparkService.get(AMBARI_API_ROOT + "/clusters/:cluster/hosts/:hostname", new AmbariHostResponse());
        sparkService.get(AMBARI_API_ROOT + "/clusters", new AmbariClusterResponse(instanceMap, clusterName));
        sparkService.get(AMBARI_API_ROOT + "/clusters/" + clusterName, new AmbariClustersHostsResponse(instanceMap, "SUCCESSFUL"));
        sparkService.post(AMBARI_API_ROOT + "/clusters/" + clusterName, new EmptyAmbariClusterResponse());
        sparkService.get(AMBARI_API_ROOT + "/clusters/:cluster/hosts/:hostname/host_components", new AmbariComponentStatusOnHostResponse());
        sparkService.get(AMBARI_API_ROOT + "/clusters/" + clusterName + "/configurations/service_config_versions",
                new AmbariServiceConfigResponse("localhost", sparkServer.getPort()), gson()::toJson);
        sparkService.get(AMBARI_API_ROOT + "/blueprints/:blueprintname", (request, response) -> {
            response.type("text/plain");
            return responseFromJsonFile("blueprint/" + request.params("blueprintname") + ".bp");
        });
    }
}
