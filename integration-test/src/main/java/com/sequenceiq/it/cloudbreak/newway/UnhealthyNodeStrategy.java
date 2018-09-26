package com.sequenceiq.it.cloudbreak.newway;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.cloudbreak.api.model.FailureReport;
import com.sequenceiq.cloudbreak.api.model.stack.StackResponse;
import com.sequenceiq.cloudbreak.api.model.stack.instance.InstanceGroupResponse;
import com.sequenceiq.cloudbreak.client.ConfigKey;
import com.sequenceiq.it.IntegrationTestContext;

public class UnhealthyNodeStrategy implements Strategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnhealthyNodeStrategy.class);

    private final String hostgroup;

    private final int nodeCount;

    public UnhealthyNodeStrategy(String hostgroup, int nodeCount) {
        this.hostgroup = hostgroup;
        this.nodeCount = nodeCount;
    }

    @Override
    public void doAction(IntegrationTestContext integrationTestContext, Entity entity) throws Exception {
        Stack stack = (Stack) entity;
        StackResponse response = Objects.requireNonNull(stack.getResponse(), "Stack response is null; should get it before");
        Long id = Objects.requireNonNull(response.getId());

        InstanceGroupResponse instanceGroup = response.getInstanceGroups().stream()
                .filter(ig -> ig.getGroup().equals(hostgroup)).collect(Collectors.toList()).get(0);
        List<String> nodes = instanceGroup.getMetadata().stream()
                .map(metadata -> metadata.getDiscoveryFQDN()).collect(Collectors.toList()).subList(0, nodeCount);
        CloudbreakClient client = getAutoscaleCloudbreakClient(integrationTestContext);
        FailureReport failureReport = new FailureReport();
        failureReport.setFailedNodes(nodes);
        client.getCloudbreakClient().autoscaleEndpoint().failureReport(id, failureReport);
    }

    private CloudbreakClient getAutoscaleCloudbreakClient(IntegrationTestContext integrationTestContext) {
        String autoscaleClientSecret = integrationTestContext.getContextParam(CloudbreakTest.AUTOSCALE_SECRET);
        String autoscaleClientId = integrationTestContext.getContextParam(CloudbreakTest.AUTOSCALE_CLIENTID);
        if (StringUtils.isNotEmpty(autoscaleClientId) && StringUtils.isNotEmpty(autoscaleClientSecret)) {
            CloudbreakClient client = new CloudbreakClient();
            client.setCloudbreakClient(new ProxyCloudbreakClient(
                    integrationTestContext.getContextParam(CloudbreakTest.CLOUDBREAK_SERVER_ROOT),
                    integrationTestContext.getContextParam(CloudbreakTest.IDENTITY_URL),
                    autoscaleClientSecret,
                    autoscaleClientId,
                    new ConfigKey(false, true, true)));
            return client;
        } else {
            LOGGER.error("Cannot create Autoscale client, fall back to default client.");
            return CloudbreakClient.getTestContextCloudbreakClient().apply(integrationTestContext);
        }

    }
}
