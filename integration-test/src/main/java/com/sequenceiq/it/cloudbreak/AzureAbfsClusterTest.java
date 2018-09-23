package com.sequenceiq.it.cloudbreak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.cloud.AzureCloudProvider;
import com.sequenceiq.it.cloudbreak.newway.cloud.CloudProviderHelper;



public class AzureAbfsClusterTest extends CloudbreakTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterTests.class);

    private static final int DESIRED_NO = 2;

    private AzureCloudProvider azureCloudProvider = null;

    @Test( priority = 10)
    @Parameters({"clusterName"})
    public void testCreateClusterWithAbfs(String clusterName) throws Exception {
        azureCloudProvider = new AzureCloudProvider(getTestParameter());
        given(CloudbreakClient.isCreated());
        given(azureCloudProvider.aValidCredential());
        given(azureCloudProvider.aValidClusterWithFs());
        given(azureCloudProvider.aValidStackRequest()
                .withName(clusterName), "a stack request");
        when(Stack.post(), "post the stack request");
        then(Stack.waitAndCheckClusterAndStackAvailabilityStatus(),
                "wait and check availability");
        then(Stack.checkClusterHasAmbariRunning(
                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PORT),
                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_USER),
                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PASSWORD)),
                "check ambari is running and components available");
    }


//    @Test(alwaysRun = true, dataProvider = "providernamekerberos", priority = 50)
//    public void testTerminateCluster(CloudProvider cloudProvider, String clusterName, boolean enableKerberos) throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(cloudProvider.aValidStackIsCreated()
//                .withName(clusterName), "a stack is created");
//        if (enableKerberos) {
//            when(Stack.delete(StackV3Action::deleteWithKerberos));
//        } else {
//            when(Stack.delete());
//        }
//        then(Stack.waitAndCheckClusterDeleted(), "stack has been deleted");
//    }
}
