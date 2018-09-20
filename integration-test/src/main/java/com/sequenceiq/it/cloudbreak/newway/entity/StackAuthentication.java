package com.sequenceiq.it.cloudbreak.newway.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.api.model.stack.StackAuthenticationRequest;
import com.sequenceiq.cloudbreak.api.model.stack.StackAuthenticationResponse;
import com.sequenceiq.it.cloudbreak.newway.AbstractCloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.ApplicationContextProvider;

@Component
@Scope("prototype")
public class StackAuthentication extends AbstractCloudbreakEntity<StackAuthenticationRequest, StackAuthenticationResponse> {

    protected StackAuthentication() {
        super("STACKAUTHENTICATION");
        setRequest(new StackAuthenticationRequest());
    }

    public static StackAuthentication valid() {
        return ApplicationContextProvider.getBean(StackAuthentication.class)
                .withPublicKeyId("publicKeyId");
    }

    public StackAuthentication withPublicKey(String publicKey) {
        getRequest().setPublicKey(publicKey);
        return this;
    }

    public StackAuthentication withPublicKeyId(String publicKeyId) {
        getRequest().setPublicKeyId(publicKeyId);
        return this;
    }

    public StackAuthentication withLoginUserName(String loginUserName) {
        getRequest().setLoginUserName(loginUserName);
        return this;
    }
}
