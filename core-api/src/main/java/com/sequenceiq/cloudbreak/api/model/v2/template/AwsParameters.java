package com.sequenceiq.cloudbreak.api.model.v2.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sequenceiq.cloudbreak.doc.ModelDescriptions.TemplateModelDescription;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class AwsParameters extends BaseTemplateParameter {

    @ApiModelProperty(TemplateModelDescription.AWS_SPOT_PRICE)
    private Double spotPrice;

    @ApiModelProperty(TemplateModelDescription.ENCRYPTION)
    private AwsEncryption awsEncryption;

    @ApiModelProperty(TemplateModelDescription.ENCRYPTED)
    private Boolean encrypted;

    public AwsEncryption getAwsEncryption() {
        return awsEncryption;
    }

    public void setAwsEncryption(AwsEncryption awsEncryption) {
        this.awsEncryption = awsEncryption;
    }

    public Boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }

    public Double getSpotPrice() {
        return spotPrice;
    }

    public void setSpotPrice(Double spotPrice) {
        this.spotPrice = spotPrice;
    }

}
