package com.sequenceiq.it.cloudbreak.newway.entity;

import static com.sequenceiq.cloudbreak.doc.ModelDescriptions.HostGroupModelDescription.RECOVERY_MODE;
import static com.sequenceiq.it.cloudbreak.newway.cloud.HostGroupType.COMPUTE;
import static com.sequenceiq.it.cloudbreak.newway.cloud.HostGroupType.MASTER;
import static com.sequenceiq.it.cloudbreak.newway.cloud.HostGroupType.WORKER;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.api.model.RecoveryMode;
import com.sequenceiq.cloudbreak.api.model.stack.instance.InstanceGroupResponse;
import com.sequenceiq.cloudbreak.api.model.stack.instance.InstanceGroupType;
import com.sequenceiq.cloudbreak.api.model.v2.InstanceGroupV2Request;
import com.sequenceiq.it.cloudbreak.newway.AbstractCloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.ApplicationContextProvider;
import com.sequenceiq.it.cloudbreak.newway.cloud.HostGroupType;

@Component
@Scope("prototype")
public class InstanceGroupEntity extends AbstractCloudbreakEntity<InstanceGroupV2Request, InstanceGroupResponse> {

    private static final String AUTO = "auto";

    private static final String MANUAL = "manual";

    protected InstanceGroupEntity() {
        super(InstanceGroupEntity.class.getSimpleName().toUpperCase());
        setRequest(new InstanceGroupV2Request());
    }

    public static List<InstanceGroupEntity> valid() {
        return valid(MASTER, COMPUTE, WORKER);
    }

    public static List<InstanceGroupEntity> valid(HostGroupType... groupTypes) {
        return Stream.of(groupTypes)
                .map(InstanceGroupEntity::create)
                .collect(Collectors.toList());
    }

    private static InstanceGroupEntity create(HostGroupType hostGroupType) {
        InstanceGroupEntity bean = ApplicationContextProvider.getBean(InstanceGroupEntity.class);
        return bean.withRecoveryMode(bean.getRecoveryModeParam(hostGroupType))
                .withNodeCount(hostGroupType.determineInstanceCount(bean.getTestParameter()))
                .withGroup(hostGroupType.getName())
                .withSecurityGroup(SecurityGroupEntity.valid())
                .withType(hostGroupType.getInstanceGroupType())
                .withTemplate(bean.getCloudProvider().template());
    }

    public InstanceGroupEntity withNodeCount(int nodeCount) {
        getRequest().setNodeCount(nodeCount);
        return this;
    }

    public InstanceGroupEntity withGroup(String group) {
        getRequest().setGroup(group);
        return this;
    }

    public InstanceGroupEntity withType(InstanceGroupType instanceGroupType) {
        getRequest().setType(instanceGroupType);
        return this;
    }

    public InstanceGroupEntity withSecurityGroup(SecurityGroupEntity securityGroup) {
        getRequest().setSecurityGroup(securityGroup.getRequest());
        return this;
    }

    public InstanceGroupEntity withTemplate(TemplateEntity template) {
        getRequest().setTemplate(template.getRequest());
        return this;
    }

    public InstanceGroupEntity withRecoveryMode(RecoveryMode recoveryMode) {
        getRequest().setRecoveryMode(recoveryMode);
        return this;
    }

    private RecoveryMode getRecoveryModeParam(HostGroupType hostGroupType) {
        String argumentName = String.join("", hostGroupType.getName(), RECOVERY_MODE);
        String argumentValue = getTestParameter().getWithDefault(argumentName, MANUAL);
        if (argumentValue.equals(AUTO)) {
            return RecoveryMode.AUTO;
        } else {
            return RecoveryMode.MANUAL;
        }
    }
}
