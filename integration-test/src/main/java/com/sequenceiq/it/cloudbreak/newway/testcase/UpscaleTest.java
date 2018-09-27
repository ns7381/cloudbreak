package com.sequenceiq.it.cloudbreak.newway.testcase;


import static com.sequenceiq.it.cloudbreak.newway.mock.model.AmbariMock.CLUSTERS_CLUSTER_REQUESTS_REQUEST;
import static com.sequenceiq.it.spark.ITResponse.AMBARI_API_ROOT;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.newway.CredentialEntity;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.action.CredentialCreateAction;
import com.sequenceiq.it.cloudbreak.newway.action.ImageCatalogCreateIfNotExistsAction;
import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.entity.AmbariEntity;
import com.sequenceiq.it.cloudbreak.newway.entity.ClusterEntity;
import com.sequenceiq.it.cloudbreak.newway.entity.StackScaleEntity;
import com.sequenceiq.it.cloudbreak.newway.v3.CredentialV3Action;
import com.sequenceiq.it.cloudbreak.newway.v3.StackV3Action;

import spark.Route;

public class UpscaleTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpscaleTest.class);

    @BeforeMethod
    public void beforeMethod(Object[] data) {
        TestContext testContext = (TestContext) data[0];
        modifySparkMock(testContext);
        testContext.given();
        testContext.given(ImageCatalog.class).withUrl(testContext.getImgCatalog().getImgCatalogUrl())
                .when(new ImageCatalogCreateIfNotExistsAction())
                .when(ImageCatalog::putSetDefaultByName)
                .given(CredentialEntity.class).withParameters(Map.of("mockEndpoint", testContext.getSparkServer().getEndpoint()))
                .when(new CredentialCreateAction());
    }

    private void modifySparkMock(TestContext testContext) {
        Route customResponse2 = (request, response) -> {
            response.status(404);
            return response;
        };

        testContext.getModel().getAmbariMock().getDynamicRouteStack().get(AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
        testContext.getModel().getAmbariMock().getDynamicRouteStack().get(AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
        testContext.getModel().getAmbariMock().getDynamicRouteStack().get(AMBARI_API_ROOT + CLUSTERS_CLUSTER_REQUESTS_REQUEST,
                customResponse2);
    }

    @AfterMethod(alwaysRun = true)
    public void tear(Object[] data) {
        TestContext testContext = (TestContext) data[0];
        testContext.when(CredentialEntity.class, CredentialV3Action::deleteV2);
        testContext.when(ImageCatalog.class, ImageCatalog::deleteV2);
    }

    @Test(enabled = false)
    public void testStackScaling(TestContext testContext, SparkServer sparkServer) throws Exception {
        // GIVEN
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster-scaling";
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackScaleEntity.class).valid()
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(sparkServer.getPort())
                .when(Stack.postV2())
                .await(STACK_AVAILABLE)
                .withName("laci")
//                .when(StackScaleEntity)
                .when(Stack.postV2());


//        StackScaleRequestV2 stackScaleRequestV2 = new StackScaleRequestV2();
//        stackScaleRequestV2.setGroup(hostGroup);
//        stackScaleRequestV2.setDesiredCount(desiredCount);
//        // WHEN
//        Response response = getCloudbreakClient().stackV2Endpoint().putScaling(stackName, stackScaleRequestV2);
//        // THEN
//        CloudbreakUtil.checkResponse("ScalingStackV2", response);
//        Map<String, String> desiredStatuses = new HashMap<>();
//        desiredStatuses.put("status", "AVAILABLE");
//        desiredStatuses.put("clusterStatus", "AVAILABLE");
//        CloudbreakUtil.waitAndCheckStatuses(getCloudbreakClient(), stackId, desiredStatuses);
//        ScalingUtil.checkStackScaled(getCloudbreakClient().stackV2Endpoint(), stackId, hostGroup, desiredCount);
//        if (checkAmbari) {
//            int nodeCount = ScalingUtil.getNodeCountStack(getCloudbreakClient().stackV2Endpoint(), stackId);
//            ScalingUtil.checkClusterScaled(getCloudbreakClient().stackV2Endpoint(), ambariPort, stackId, ambariUser, ambariPassword, nodeCount, itContext);
//        }
    }

    @Test(enabled = false)
    public void testCreateNewRegularCluster(TestContext testContext, SparkServer sparkServer) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(sparkServer.getPort())
                .when(Stack.postV2())
                .await(STACK_AVAILABLE)
                .when(StackEntity.class, StackV3Action::deleteV2)
                .await(STACK_DELETED);
    }

}
