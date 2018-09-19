package com.sequenceiq.it.cloudbreak.newway.ger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CredentialEntity;
import com.sequenceiq.it.cloudbreak.newway.Entity;
import com.sequenceiq.it.cloudbreak.newway.log.Log;

public class CredentialCreateStrategy implements StrategyV2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialCreateStrategy.class);

    @Override
    public void doAction(IntegrationTestContext integrationTestContext, Entity entity, CloudbreakClient client) throws Exception {
        CredentialEntity credentialEntity = (CredentialEntity) entity;
        Log.log(" post "
                .concat(credentialEntity.getName())
                .concat(" private credential. "));
        try {
            credentialEntity.setResponse(
                    client.getCloudbreakClient()
                            .credentialEndpoint()
                            .postPrivate(credentialEntity.getRequest()));
        } catch (Exception e) {
            LOGGER.info("Creation of credential has failed, using existing if it is possible", e);
        }
    }

    @Override
    public void doAction(IntegrationTestContext integrationTestContext, Entity entity) throws Exception {

    }
}
