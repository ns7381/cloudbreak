package com.sequenceiq.cloudbreak.api.model.stack.instance;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sequenceiq.cloudbreak.api.model.JsonEntity;
import com.sequenceiq.cloudbreak.doc.ModelDescriptions.InstanceGroupModelDescription;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("InstanceGroup")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class InstanceGroupBase implements JsonEntity {

    @ApiModelProperty(InstanceGroupModelDescription.TEMPLATE_ID)
    private Long templateId;

    @ApiModelProperty(InstanceGroupModelDescription.SECURITY_GROUP_ID)
    private Long securityGroupId;

    @Min(value = 0, message = "The node count has to be greater or equals than 0")
    @Max(value = 100000, message = "The node count has to be less than 100000")
    @Digits(fraction = 0, integer = 10, message = "The node count has to be a number")
    @ApiModelProperty(value = InstanceGroupModelDescription.NODE_COUNT, required = true)
    private int nodeCount;

    @NotNull
    @ApiModelProperty(value = InstanceGroupModelDescription.INSTANCE_GROUP_NAME, required = true)
    private String group;

    @ApiModelProperty(InstanceGroupModelDescription.INSTANCE_GROUP_TYPE)
    private InstanceGroupType type = InstanceGroupType.CORE;

    @ApiModelProperty(InstanceGroupModelDescription.PARAMETERS)
    private Map<String, Object> parameters = new HashMap<>();

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Long getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(Long securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public InstanceGroupType getType() {
        return type;
    }

    public void setType(InstanceGroupType type) {
        this.type = type;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
