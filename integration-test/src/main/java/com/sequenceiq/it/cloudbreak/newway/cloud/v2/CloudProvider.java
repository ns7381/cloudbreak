package com.sequenceiq.it.cloudbreak.newway.cloud.v2;

import java.util.Map;

import com.sequenceiq.it.cloudbreak.newway.entity.NetworkV2Entity;
import com.sequenceiq.it.cloudbreak.newway.entity.TemplateEntity;

public interface CloudProvider {

    String availabilityZone();

    String region();

    TemplateEntity template();

    String getVpcId();

    String getSubnetId();

    Map<String, Object> networkProperties();

    Map<String, Object> subnetProperties();

    NetworkV2Entity newNetwork();

    NetworkV2Entity existingNetwork();

    NetworkV2Entity existingSubnet();

    String getSubnetCIDR();
}
