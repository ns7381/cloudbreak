package com.sequenceiq.it.cloudbreak.newway;

import java.util.Map;

import com.sequenceiq.cloudbreak.api.model.CredentialRequest;
import com.sequenceiq.cloudbreak.api.model.CredentialResponse;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;

public class CredentialEntity extends AbstractCloudbreakEntity<CredentialRequest, CredentialResponse, CredentialEntity> {
    public static final String CREDENTIAL = "CREDENTIAL";

    CredentialEntity(TestContext testContext) {
        super(new CredentialRequest(), testContext);
    }

    CredentialEntity() {
        super(CREDENTIAL);
        setRequest(new CredentialRequest());
    }

    public CredentialEntity withName(String name) {
        getRequest().setName(name);
        setName(name);
        return this;
    }

    public CredentialEntity withDescription(String description) {
        getRequest().setDescription(description);
        return this;
    }

    public CredentialEntity withCloudPlatform(String cloudPlatform) {
        getRequest().setCloudPlatform(cloudPlatform);
        return this;
    }

    public CredentialEntity withParameters(Map<String, Object> parameters) {
        getRequest().setParameters(parameters);
        return this;
    }
}
