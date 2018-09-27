package com.sequenceiq.it.cloudbreak.newway.testcase;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.newway.CredentialEntity;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.action.CredentialCreateAction;
import com.sequenceiq.it.cloudbreak.newway.action.ImageCatalogCreateIfNotExistsAction;
import com.sequenceiq.it.cloudbreak.newway.action.StackRefreshAction;
import com.sequenceiq.it.cloudbreak.newway.action.WaitAndCheckClusterDeletedAction;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;
import com.sequenceiq.it.cloudbreak.newway.entity.AmbariEntity;
import com.sequenceiq.it.cloudbreak.newway.entity.ClusterEntity;
import com.sequenceiq.it.cloudbreak.newway.v3.CredentialV3Action;
import com.sequenceiq.it.cloudbreak.newway.v3.StackV3Action;

public class TerminationTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminationTest.class);

    @BeforeMethod
    public void beforeMethod(Object[] data) {
        TestContext testContext = (TestContext) data[0];

        testContext.given();
        testContext.given(ImageCatalog.class).withUrl(testContext.getImgCatalog().getImgCatalogUrl())
                .when(new ImageCatalogCreateIfNotExistsAction())
                .when(ImageCatalog::putSetDefaultByName)
                .given(CredentialEntity.class)
                .withParameters(Map.of("mockEndpoint", testContext.getSparkServer().getEndpoint()))
                .when(new CredentialCreateAction());

    }

    @AfterMethod(alwaysRun = true)
    public void tear(Object[] data) {
        TestContext testContext = (TestContext) data[0];
        testContext.getSparkServer().stop();
        testContext.when(CredentialEntity.class, CredentialV3Action::deleteV2, false);
        testContext.when(ImageCatalog.class, ImageCatalog::deleteV2, false);

        if (!testContext.getErrors().isEmpty()) {
            testContext.getErrors().forEach(LOGGER::error);
            Assert.fail("See exceptions below");
        }
    }

    @Test(dataProvider = "testContext")
    public void testCreateNewRegularCluster(TestContext testContext) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(testContext.getSparkServer().getPort())
                .when(Stack.postV2())
                .await(STACK_AVAILABLE)
                .when(StackEntity.class, StackV3Action::deleteV2, false)
                .then(WaitAndCheckClusterDeletedAction.create());
    }

    @Test(dataProvider = "testContext")
    public void testInstanceTermination(TestContext testContext) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        testContext
                // create stack
                .given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(testContext.getSparkServer().getPort())
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
    public void testInstanceTermination2(TestContext testContext) {
        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        testContext.given(ClusterEntity.class).withName(clusterName)
                .given(AmbariEntity.class).withBlueprintName(blueprintName)
                .given(StackEntity.class).withName(clusterName).withGatewayPort(testContext.getSparkServer().getPort())
                .when(Stack.postV2())
                .then(Stack::waitAndCheckClusterAndStackAvailabilityStatusV2);


        String hostGroupName = "worker";
        StackEntity stack = testContext.get(StackEntity.class);
        testContext.when(stack, new StackRefreshAction());
        int before = stack.getInstanceMetaData(hostGroupName).size();

        stack.when(Stack.deleteInstance(stack.getInstanceId(hostGroupName)))
                .then(Stack::waitAndCheckClusterAndStackAvailabilityStatusV2);

        stack.when(new StackRefreshAction());
        int after = stack.getInstanceMetaData(hostGroupName).size();

        stack.when(StackEntity.class, StackV3Action::deleteV2, false)
                .then(Stack::waitAndCheckClusterDeletedV2, false);

        Assert.assertEquals(after, before - 1);
    }
}
