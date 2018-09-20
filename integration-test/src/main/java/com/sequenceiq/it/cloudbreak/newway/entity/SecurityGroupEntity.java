package com.sequenceiq.it.cloudbreak.newway.entity;

import java.util.List;
import java.util.stream.Collectors;

import com.sequenceiq.cloudbreak.api.model.SecurityGroupResponse;
import com.sequenceiq.cloudbreak.api.model.v2.SecurityGroupV2Request;
import com.sequenceiq.it.cloudbreak.newway.AbstractCloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.SecurityRules;

public class SecurityGroupEntity extends AbstractCloudbreakEntity<SecurityGroupV2Request, SecurityGroupResponse> {

    protected SecurityGroupEntity() {
        super(SecurityGroupEntity.class.getSimpleName().toUpperCase());
        setRequest(new SecurityGroupV2Request());
    }

    public static SecurityGroupEntity valid() {
        return new SecurityGroupEntity()
                .withSecurityRules(SecurityRules.valid());
    }

    public SecurityGroupEntity withSecurityRules(List<SecurityRules> securityRules) {

        getRequest().setSecurityRules(securityRules.stream().map(SecurityRules::getRequest).collect(Collectors.toList()));
        return this;
    }

    public SecurityGroupEntity withSecurityGroupIds(List<String> securityGroupIds) {
        return this;
    }
}
