package com.sequenceiq.it.cloudbreak.newway.strategy;

import static com.sequenceiq.it.cloudbreak.newway.log.Log.log;
import static com.sequenceiq.it.cloudbreak.newway.log.Log.logJSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.Stack;
import com.sequenceiq.it.cloudbreak.newway.ger.StrategyV2;

public class StackPostStrategy implements StrategyV2<Stack> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackPostStrategy.class);

    private static final String SUBNET_ID_KEY = "subnetId";

    private static final String NETWORK_ID_KEY = "networkId";

    @Override
    public void doAction(Long workspaceId, Stack entity, CloudbreakClient client) throws Exception {

        /*Credential credential = Credential.getTestContextCredential().apply(integrationTestContext);

        if (credential != null && entity.getRequest().getGeneral().getCredentialName() == null) {
            entity.getRequest().getGeneral().setCredentialName(credential.getName());
        }

        Cluster cluster = Cluster.getTestContextCluster().apply(integrationTestContext);
        if (cluster != null) {
            if (entity.getRequest().getCluster() == null) {
                entity.getRequest().setCluster(cluster.getRequest());
            }
            if (cluster.getRequest().getCloudStorage() != null && cluster.getRequest().getCloudStorage().getS3()
                    != null && isEmpty(cluster.getRequest().getCloudStorage().getS3().getInstanceProfile())) {
                AccessConfig accessConfig = AccessConfig.getTestContextAccessConfig().apply(integrationTestContext);
                List<String> arns = accessConfig
                        .getResponse()
                        .getAccessConfigs()
                        .stream()
                        .map(accessConfigJson -> accessConfigJson.getProperties().get("arn").toString())
                        .sorted()
                        .distinct()
                        .collect(Collectors.toList());
                cluster.getRequest().getCloudStorage().getS3().setInstanceProfile(arns.get(0));
            } else if (cluster.getRequest().getCloudStorage() != null && cluster.getRequest().getCloudStorage().getGcs() != null && credential != null) {
                cluster.getRequest().getCloudStorage().getGcs()
                        .setServiceAccountEmail(credential.getResponse().getParameters().get("serviceAccountId").toString());
            }
        }

        Integer gatewayPort = integrationTestContext.getContextParam("MOCK_PORT", Integer.class);
        if (gatewayPort != null) {
            entity.getRequest().setGatewayPort(gatewayPort);
        }

        Kerberos kerberos = Kerberos.getTestContextCluster().apply(integrationTestContext);
        boolean updateKerberos = entity.getRequest().getCluster() != null && stackEntity.getRequest().getCluster().getAmbari() != null
                && entity.getRequest().getCluster().getAmbari().getKerberos() == null;
        if (kerberos != null && updateKerberos) {
            AmbariV2Request ambariReq = stackEntity.getRequest().getCluster().getAmbari();
            ambariReq.setEnableSecurity(true);
            ambariReq.setKerberos(kerberos.getRequest());
        }

        ClusterGateway clusterGateway = ClusterGateway.getTestContextGateway().apply(integrationTestContext);
        if (clusterGateway != null) {
            if (entity.hasCluster()) {
                ClusterV2Request clusterV2Request = stackEntity.getRequest().getCluster();
                AmbariV2Request ambariV2Request = clusterV2Request.getAmbari();
                if (ambariV2Request != null) {
                    ambariV2Request.setGateway(clusterGateway.getRequest());
                }
            }
        }

        ImageSettings imageSettings = ImageSettings.getTestContextImageSettings().apply(integrationTestContext);
        if (imageSettings != null) {
            entity.getRequest().setImageSettings(imageSettings.getRequest());
        }

        HostGroups hostGroups = HostGroups.getTestContextHostGroups().apply(integrationTestContext);
        if (hostGroups != null) {
            entity.getRequest().setInstanceGroups(hostGroups.getRequest());
        }

        var datalakeStack = DatalakeCluster.getTestContextDatalakeCluster().apply(integrationTestContext);
        if (datalakeStack != null && datalakeStack.getResponse() != null && datalakeStack.getResponse().getNetwork() != null) {
            String subnetId = null;
            String networkId = null;
            var properties = Optional.ofNullable(datalakeStack.getResponse().getNetwork().getParameters());
            if (properties.isPresent()) {
                if (!isEmpty((CharSequence) properties.get().get(SUBNET_ID_KEY))) {
                    subnetId = properties.get().get(SUBNET_ID_KEY).toString();
                }
                if (!isEmpty((CharSequence) properties.get().get(NETWORK_ID_KEY))) {
                    networkId = properties.get().get(NETWORK_ID_KEY).toString();
                }
            }
            if (entity.getRequest().getNetwork() != null && entity.getRequest().getNetwork().getParameters() != null) {
                entity.getRequest().getNetwork().getParameters().put(SUBNET_ID_KEY, subnetId);
                entity.getRequest().getNetwork().getParameters().put(NETWORK_ID_KEY, networkId);
            } else {
                var network = new NetworkV2Request();
                var params = new LinkedHashMap<String, Object>();
                params.put(SUBNET_ID_KEY, subnetId);
                params.put(NETWORK_ID_KEY, networkId);
                network.setParameters(params);
                entity.getRequest().setNetwork(network);
            }
            entity.getRequest().getNetwork().setSubnetCIDR(null);
            entity.getRequest().getNetwork().getParameters().put("routerId", null);
            entity.getRequest().getNetwork().getParameters().put("publicNetId", null);
            entity.getRequest().getNetwork().getParameters().put("noPublicIp", false);
            entity.getRequest().getNetwork().getParameters().put("noFirewallRules", false);
            entity.getRequest().getNetwork().getParameters().put("internetGatewayId", null);
        }*/

        log(" Name:\n" + entity.getRequest().getGeneral().getName());
        logJSON(" Stack post request:\n", entity.getRequest());
        entity.setResponse(
                client.getCloudbreakClient()
                        .stackV3Endpoint()
                        .createInWorkspace(workspaceId, entity.getRequest()));
        logJSON(" Stack post response:\n", entity.getResponse());
        log(" ID:\n" + entity.getResponse().getId());
    }
}
