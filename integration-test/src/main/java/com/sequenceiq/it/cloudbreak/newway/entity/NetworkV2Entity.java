package com.sequenceiq.it.cloudbreak.newway.entity;

import java.util.Map;

import com.sequenceiq.cloudbreak.api.model.NetworkResponse;
import com.sequenceiq.cloudbreak.api.model.v2.NetworkV2Request;
import com.sequenceiq.it.cloudbreak.newway.AbstractCloudbreakEntity;

public class NetworkV2Entity extends AbstractCloudbreakEntity<NetworkV2Request, NetworkResponse> {
    public static final String NETWORK = "NETWORK";

    NetworkV2Entity(String newId) {
        super(newId);
        setRequest(new NetworkV2Request());
    }

    NetworkV2Entity() {
        this(NETWORK);
    }

    public static NetworkV2Entity valid() {
        return new NetworkV2Entity();
    }

    public NetworkV2Entity withParameters(Map<String, Object> parameters) {
        getRequest().setParameters(parameters);
        return this;
    }

    public NetworkV2Entity withSubnetCIDR(String subnetCIDR) {
        getRequest().setSubnetCIDR(subnetCIDR);
        return this;
    }
}
