package com.sequenceiq.it.cloudbreak.newway.mock.model;

import static com.sequenceiq.it.cloudbreak.newway.Mock.gson;
import static com.sequenceiq.it.cloudbreak.newway.Mock.responseFromJsonFile;
import static com.sequenceiq.it.spark.ITResponse.AMBARI_API_ROOT;

import java.util.Map;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sequenceiq.cloudbreak.cloud.model.CloudVmMetaDataStatus;
import com.sequenceiq.it.cloudbreak.newway.mock.AbstractModelMock;
import com.sequenceiq.it.cloudbreak.newway.mock.DefaultModel;
import com.sequenceiq.it.spark.DynamicRouteStack;
import com.sequenceiq.it.spark.ITResponse;
import com.sequenceiq.it.spark.ambari.AmbariCheckResponse;
import com.sequenceiq.it.spark.ambari.AmbariClusterRequestsResponse;
import com.sequenceiq.it.spark.ambari.AmbariClusterResponse;
import com.sequenceiq.it.spark.ambari.AmbariClustersHostsResponseW;
import com.sequenceiq.it.spark.ambari.AmbariComponentStatusOnHostResponse;
import com.sequenceiq.it.spark.ambari.AmbariHostsResponseV2;
import com.sequenceiq.it.spark.ambari.AmbariServiceConfigResponseV2;
import com.sequenceiq.it.spark.ambari.AmbariServicesComponentsResponse;
import com.sequenceiq.it.spark.ambari.AmbariStatusResponse;
import com.sequenceiq.it.spark.ambari.AmbariVersionDefinitionResponse;
import com.sequenceiq.it.spark.ambari.AmbariViewResponse;
import com.sequenceiq.it.spark.ambari.EmptyAmbariClusterResponse;
import com.sequenceiq.it.spark.ambari.EmptyAmbariResponse;
import com.sequenceiq.it.spark.ambari.v2.AmbariCategorizedHostComponentStateResponse;
import com.sequenceiq.it.util.HostNameUtil;

import spark.Service;

public class AmbariMock extends AbstractModelMock {

    public static final String CLUSTERS_CLUSTER = "/clusters/:cluster";

    public static final String ROOT_CLUSTERS_CLUSTER = AMBARI_API_ROOT + CLUSTERS_CLUSTER;

    public static final String CLUSTERS_CLUSTER_NAME_CONFIGURATIONS_SERVICE_CONFIG_VERSIONS = ROOT_CLUSTERS_CLUSTER + "/configurations/service_config_versions";

    public static final String CLUSTERS_CLUSTER_HOSTS = ROOT_CLUSTERS_CLUSTER + "/hosts";

    public static final String CLUSTERS_CLUSTER_HOSTS_HOSTNAME_HOST_COMPONENTS = ROOT_CLUSTERS_CLUSTER + "/hosts/:hostname/host_components/*";

    public static final String CLUSTERS_CLUSTER_HOSTS_INTERNALHOSTNAME = ROOT_CLUSTERS_CLUSTER + "/hosts/:internalhostname";

    public static final String CLUSTERS_CLUSTER_HOST_COMPONENTS = ROOT_CLUSTERS_CLUSTER + "/host_components";

    public static final String CLUSTERS_CLUSTER_HOSTS_HOSTNAME = ROOT_CLUSTERS_CLUSTER + "/hosts/:hostname";

    public static final String CLUSTERS_CLUSTER_SERVICES = ROOT_CLUSTERS_CLUSTER + "/services/*";

    public static final String CLUSTERS_CLUSTER_SERVICES_HDFS_COMPONENTS_NAMENODE = ROOT_CLUSTERS_CLUSTER + "/services/HDFS/components/NAMENODE";

    public static final String CLUSTERS_CLUSTER_REQUESTS_REQUEST = ROOT_CLUSTERS_CLUSTER + "/requests/:request";

    public static final String CLUSTERS_CLUSTER_REQUESTS = ROOT_CLUSTERS_CLUSTER + "/requests";

    public static final String STACKS_HDP_VERSIONS_VERSION_OPERATING_SYSTEMS_OS_REPOSITORIES_HDPVERSION
            = "/stacks/HDP/versions/:version/operating_systems/:os/repositories/:hdpversion";

    public static final String USERS = AMBARI_API_ROOT + "/users";

    public static final String USERS_ADMIN = AMBARI_API_ROOT + "/users/admin";

    public static final String BLUEPRINTS = AMBARI_API_ROOT + "/blueprints/*";

    public static final String BLUEPRINTS_BLUEPRINTNAME = AMBARI_API_ROOT + "/blueprints/:blueprintname";

    public static final String SERVICES_AMBARI_COMPONENTS_AMBARI_SERVER = AMBARI_API_ROOT + "/services/AMBARI/components/AMBARI_SERVER";

    public static final String VIEWS_VIEW_VERSIONS_1_0_0_INSTANCES = AMBARI_API_ROOT + "/views/:view/versions/1.0.0/instances/*";

    public static final String CHECK = AMBARI_API_ROOT + "/check";

    public static final String VIEWS = AMBARI_API_ROOT + "/views/*";

    private DynamicRouteStack dynamicRouteStack;

    public AmbariMock(Service sparkService, DefaultModel defaultModel) {
        super(sparkService, defaultModel);
        dynamicRouteStack = new DynamicRouteStack(getSparkService(), getDefaultModel());
    }

    public void addAmbariMappings() {
        Map<String, CloudVmMetaDataStatus> instanceMap = getDefaultModel().getInstanceMap();
        Service sparkService = getSparkService();

        getAmbariClusterRequest(sparkService);
        getAmbariClusters(getDefaultModel().getClusterName(), instanceMap, sparkService);
        postAmbariClusterRequest(sparkService);
        getAmbariCheck(sparkService);
        postAmbariUsers(sparkService);
        postAmbariCluster(sparkService);
        getAmbariBlueprint(sparkService);
        getAmbariClusterHosts(instanceMap, sparkService, "STARTED");
        getAmbariHosts(instanceMap, sparkService);
        postAmbariInstances(sparkService);
        postAmbariClusters(sparkService);
        getAmbariComponents(sparkService);
        postAmbariBlueprints(sparkService);
        putAmbariUsersAdmin(sparkService);
        getAmbariClusterHosts(instanceMap, sparkService);
        putAmbariHdpVersion(sparkService);
        getAmabriVersionDefinitions(sparkService);
        postAmbariVersionDefinitions(sparkService);

        getAmbariCluster(getDefaultModel().getClusterName(), instanceMap, sparkService);
        getAmbariClusterHosts(instanceMap, sparkService, "INSTALLED");
        putAmbariClusterServices(sparkService);
        postAmbariClusterHosts(sparkService);
        getAmbariClusterHostComponents(sparkService);
        getAmbariClusterConfigurationVersions(sparkService);
        getAmbariClusterHostStatus(sparkService);
        getAmbariClusterServicesComponentsNamenode(instanceMap, sparkService);
        putAmbariClusterHostComponents(sparkService);
        deleteClusterHostComponents(sparkService);
        deleteAmbariClusterHost(sparkService);
        getAmbariViews(sparkService);
    }

    public DynamicRouteStack getDynamicRouteStack() {
        return dynamicRouteStack;
    }

    private void postAmbariClusters(Service sparkService) {
        sparkService.post(AMBARI_API_ROOT + "/clusters/:cluster", (req, resp) -> {
            getDefaultModel().setClusterCreated(true);
            return new EmptyAmbariResponse().handle(req, resp);
        }, gson()::toJson);
    }

    private void getAmabriVersionDefinitions(Service sparkService) {
        dynamicRouteStack.get(AMBARI_API_ROOT + "/version_definitions", new AmbariVersionDefinitionResponse());
    }

    private void getAmbariClusterHosts(Map<String, CloudVmMetaDataStatus> instanceMap, Service sparkService) {
        dynamicRouteStack.get(AMBARI_API_ROOT + "/clusters/:cluster/hosts",
                new AmbariCategorizedHostComponentStateResponse(instanceMap));
    }

    private void getAmbariClusterHosts2(Map<String, CloudVmMetaDataStatus> instanceMap, Service sparkService) {
        getAmbariClusterHosts(instanceMap, sparkService, "INSTALLED");
    }

    private void getAmbariClusterHosts(Map<String, CloudVmMetaDataStatus> instanceMap, Service sparkService, String state) {
        dynamicRouteStack.get(AMBARI_API_ROOT + "/clusters/:cluster/hosts",
                new AmbariClustersHostsResponseW(instanceMap, state));
    }

    private void getAmbariHosts(Map<String, CloudVmMetaDataStatus> instanceMap, Service sparkService) {
        dynamicRouteStack.get(AMBARI_API_ROOT + "/hosts", new AmbariHostsResponseV2());
    }

    private void getAmbariClusters(String clusterName, Map<String, CloudVmMetaDataStatus> instanceMap, Service sparkService) {
        dynamicRouteStack.get(AMBARI_API_ROOT + "/clusters", (req, resp) -> {
            ITResponse itResp = getDefaultModel().isClusterCreated() ? new AmbariClusterResponse(instanceMap, clusterName) : new EmptyAmbariClusterResponse();
            return itResp.handle(req, resp);
        });
    }

    private void getAmbariClusterServicesComponentsNamenode(Map<String, CloudVmMetaDataStatus> instanceMap, Service sparkService) {
        dynamicRouteStack.get(CLUSTERS_CLUSTER_SERVICES_HDFS_COMPONENTS_NAMENODE, (request, response) -> {
            response.type("text/plain");
            ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
            ObjectNode nameNode = rootNode.putObject("metrics").putObject("dfs").putObject("namenode");
            ObjectNode liveNodesRoot = JsonNodeFactory.instance.objectNode();

            for (CloudVmMetaDataStatus status : instanceMap.values()) {
                ObjectNode node = liveNodesRoot.putObject(HostNameUtil.generateHostNameByIp(status.getMetaData().getPrivateIp()));
                node.put("remaining", "10000000");
                node.put("usedSpace", Integer.toString(100000));
                node.put("adminState", "In Service");
            }

            nameNode.put("LiveNodes", liveNodesRoot.toString());
            nameNode.put("DecomNodes", "{}");
            return rootNode;
        });
    }

    private void getAmbariClusterHostStatus(Service sparkService) {
        dynamicRouteStack.get(CLUSTERS_CLUSTER_HOSTS_INTERNALHOSTNAME, (request, response) -> {
            response.type("text/plain");
            ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
            rootNode.putObject("Hosts").put("public_host_name", request.params("internalhostname")).put("host_status", "HEALTHY");
            return rootNode;
        });
    }

    private void getAmbariClusterConfigurationVersions(Service sparkService) {
        dynamicRouteStack.get(CLUSTERS_CLUSTER_NAME_CONFIGURATIONS_SERVICE_CONFIG_VERSIONS,
                new AmbariServiceConfigResponseV2());
    }

    private void getAmbariClusterHostComponents(Service sparkService) {
        dynamicRouteStack.get(CLUSTERS_CLUSTER_HOSTS_HOSTNAME_HOST_COMPONENTS,
                new AmbariComponentStatusOnHostResponse());
    }

    private void postAmbariClusterHosts(Service sparkService) {
        sparkService.post(CLUSTERS_CLUSTER_HOSTS, new AmbariClusterRequestsResponse());
    }

    private void putAmbariClusterServices(Service sparkService) {
        dynamicRouteStack.put(CLUSTERS_CLUSTER_SERVICES, new AmbariClusterRequestsResponse());
    }

    private void getAmbariCluster(String clusterName, Map<String, CloudVmMetaDataStatus> instanceMap, Service sparkService) {
        dynamicRouteStack.get(CLUSTERS_CLUSTER, new AmbariClusterResponse(instanceMap, clusterName));
    }

    private void getAmbariViews(Service sparkService) {
        dynamicRouteStack.get(VIEWS, new AmbariViewResponse(getDefaultModel().getMockServerAddress()));
    }

    private void postAmbariVersionDefinitions(Service sparkService) {
        sparkService.post(AMBARI_API_ROOT + "/version_definitions", new EmptyAmbariResponse());
    }

    private void putAmbariHdpVersion(Service sparkService) {
        dynamicRouteStack.put(STACKS_HDP_VERSIONS_VERSION_OPERATING_SYSTEMS_OS_REPOSITORIES_HDPVERSION,
                new AmbariVersionDefinitionResponse());
    }

    private void putAmbariClusterHostComponents(Service sparkService) {
        dynamicRouteStack.put(CLUSTERS_CLUSTER_HOST_COMPONENTS, new AmbariClusterRequestsResponse());
    }

    private void deleteClusterHostComponents(Service sparkService) {
        dynamicRouteStack.delete(CLUSTERS_CLUSTER_HOSTS_HOSTNAME_HOST_COMPONENTS, new EmptyAmbariResponse());
    }

    private void deleteAmbariClusterHost(Service sparkService) {
        dynamicRouteStack.delete(CLUSTERS_CLUSTER_HOSTS_HOSTNAME, new AmbariClusterRequestsResponse());
    }

    private void postAmbariUsers(Service sparkService) {
        dynamicRouteStack.post(USERS, new EmptyAmbariResponse());
    }

    private void putAmbariUsersAdmin(Service sparkService) {
        dynamicRouteStack.put(USERS_ADMIN, new EmptyAmbariResponse());
    }

    private void postAmbariBlueprints(Service sparkService) {
        dynamicRouteStack.post(BLUEPRINTS, new EmptyAmbariResponse());
    }

    private void getAmbariBlueprint(Service sparkService) {
        dynamicRouteStack.get(BLUEPRINTS_BLUEPRINTNAME, (request, response) -> {
            response.type("text/plain");
            return responseFromJsonFile("blueprint/" + request.params("blueprintname") + ".bp");
        });
    }

    private void getAmbariComponents(Service sparkService) {
        dynamicRouteStack.get(SERVICES_AMBARI_COMPONENTS_AMBARI_SERVER, new AmbariServicesComponentsResponse());
    }

    private void postAmbariCluster(Service sparkService) {
        dynamicRouteStack.post(CLUSTERS_CLUSTER, (request, response) -> {
            getDefaultModel().setClusterName(request.params("cluster"));
            response.type("text/plain");
            return "";
        });
    }

    private void postAmbariClusterRequest(Service sparkService) {
        dynamicRouteStack.post(CLUSTERS_CLUSTER_REQUESTS, new AmbariClusterRequestsResponse());
    }

    private void postAmbariInstances(Service sparkService) {
        dynamicRouteStack.post(VIEWS_VIEW_VERSIONS_1_0_0_INSTANCES, new EmptyAmbariResponse());
    }

    private void getAmbariClusterRequest(Service sparkService) {
        dynamicRouteStack.get(CLUSTERS_CLUSTER_REQUESTS_REQUEST, new AmbariStatusResponse());
    }

    private void getAmbariCheck(Service sparkService) {
        dynamicRouteStack.get(CHECK, new AmbariCheckResponse());
    }

}
