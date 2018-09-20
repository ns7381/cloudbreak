package com.sequenceiq.it.cloudbreak.newway.cloud.v2;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.sequenceiq.it.cloudbreak.newway.TestParameter;
import com.sequenceiq.it.cloudbreak.newway.entity.NetworkV2Entity;
import com.sequenceiq.it.cloudbreak.newway.entity.TemplateEntity;

@Component
public class MockCloudProvider extends AbstractCloudProvider {

    public static final String MOCK = "mock";

    public static final String MOCK_CAPITAL = "MOCK";

    public static final String MOCK_CLUSTER_DEFAULT_NAME = "autotesting-mock-cluster";

    public static final String KEY_BASED_CREDENTIAL = "key";

    private static final String CREDENTIAL_DEFAULT_NAME = "autotesting-mock-cred";

    private static final String CREDENTIAL_DEFAULT_DESCRIPTION = "autotesting mock credential";

    private static final String BLUEPRINT_DEFAULT_NAME = "Data Science: Apache Spark 2, Apache Zeppelin";

    private static final String NETWORK_DEFAULT_NAME = "autotesting-aws-net";

    private static final String VPC_DEFAULT_ID = "vpc-e623b28d";

    private static final String INTERNET_GATEWAY_ID = "igw-b55b26dd";

    private static final String SUBNET_DEFAULT_ID = "subnet-83901cfe";

    private static final String DEFAULT_SUBNET_CIDR = "10.0.0.0/16";

    private static final String NETWORK_DEFAULT_DESCRIPTION = "autotesting mock network";

    @Inject
    private TestParameter testParameter;

    @Override
    public String availabilityZone() {
        String availabilityZone = "eu-west-1a";
        String availabilityZoneParam = getTestParameter().get("mockAvailabilityZone");

        return availabilityZoneParam == null ? availabilityZone : availabilityZoneParam;
    }

    @Override
    public String region() {
        String region = "eu-west-1";
        String regionParam = getTestParameter().get("mockRegion");

        return regionParam == null ? region : regionParam;
    }

    @Override
    public TemplateEntity template() {
        String instanceTypeDefaultValue = "large";
        String instanceTypeParam = getTestParameter().get("mockInstanceType");

        int volumeCountDefault = 1;
        String volumeCountParam = getTestParameter().get("mockInstanceVolumeCount");

        int volumeSizeDefault = 100;
        String volumeSizeParam = getTestParameter().get("mockInstanceVolumeSize");

        String volumeTypeDefault = "magnetic";
        String volumeTypeParam = getTestParameter().get("mockInstanceVolumeType");

        return TemplateEntity.valid()
                .withInstanceType(instanceTypeParam == null ? instanceTypeDefaultValue : instanceTypeParam)
                .withVolumeCount(volumeCountParam == null ? volumeCountDefault : Integer.parseInt(volumeCountParam))
                .withVolumeSize(volumeSizeParam == null ? volumeSizeDefault : Integer.parseInt(volumeSizeParam))
                .withVolumeType(volumeTypeParam == null ? volumeTypeDefault : volumeTypeParam);
    }

    @Override
    public String getVpcId() {
        String vpcId = getTestParameter().get("mockVcpId");
        return vpcId == null ? VPC_DEFAULT_ID : vpcId;
    }

    @Override
    public String getSubnetId() {
        String subnetId = getTestParameter().get("mockSubnetId");
        return subnetId == null ? SUBNET_DEFAULT_ID : subnetId;
    }

    public String getInternetGatewayId() {
        String gatewayId = getTestParameter().get("mockInternetGatewayId");
        return gatewayId == null ? INTERNET_GATEWAY_ID : gatewayId;
    }

    @Override
    public Map<String, Object> networkProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("internetGatewayId", getInternetGatewayId());
        map.put("vpcId", getVpcId());
        return map;
    }

    @Override
    public Map<String, Object> subnetProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("subnetId", getSubnetId());
        map.put("vpcId", getVpcId());

        return map;
    }

    @Override
    public NetworkV2Entity newNetwork() {
        return NetworkV2Entity.valid()
                .withSubnetCIDR(getSubnetCIDR());
    }

    @Override
    public NetworkV2Entity existingNetwork() {
        return NetworkV2Entity.valid()
                .withSubnetCIDR(getSubnetCIDR())
                .withParameters(networkProperties());
    }

    @Override
    public NetworkV2Entity existingSubnet() {
        return NetworkV2Entity.valid()
                .withParameters(subnetProperties());
    }
}