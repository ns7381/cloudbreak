package com.sequenceiq.it.cloudbreak.newway.ger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.newway.Cluster;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.strategy.StackPostStrategy;
import com.sequenceiq.it.cloudbreak.newway.v3.StackV3Action;

public class TerninationTest extends AbstractIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerninationTest.class);

    @Test
    public void testCreateNewRegularCluster() throws Exception {

        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
        String clusterName = "mockcluster";
        StackEntity stackEntity = null;
        try {
            Cluster cluster = Cluster.request().withAmbariRequest(AmbariRequestTest.def().withName(blueprintName).build());
            stackEntity = given(Stack.valid().withClusterRequest(cluster.getRequest()).withName(clusterName), new StackPostStrategy(), "a stack request");
//        when(Stack.post(), "post the stack request");
//            then(Stack.waitAndCheckClusterAndStackAvailabilityStatus(), "wait and check availability");
            Stack.waitAndCheckClusterAndStackAvailabilityStatus(stackEntity, getCloudbreakClient());
//        then(Mock.assertCalls(verify(SALT_API_ROOT + "/run", "POST").bodyContains("fun=saltutil.sync_grains").atLeast(2)));
        } finally {
            if (stackEntity != null) {
                StackV3Action.delete(getItContext(), stackEntity, getCloudbreakClient(), true);
//            then(Stack.waitAndCheckClusterDeleted());
//            then(new Assertion<>(itContext -> reference.get(), Stack::waitAndCheckClusterDeletedA));
                Stack.waitAndCheckClusterDeletedA(stackEntity, getItContext(), getCloudbreakClient());
            }
        }
    }
}
