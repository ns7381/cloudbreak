package com.sequenceiq.it.cloudbreak.newway.ger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CredentialEntity;
import com.sequenceiq.it.cloudbreak.newway.log.Log;

public class CredentialCreateStrategy implements StrategyV2<CredentialEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialCreateStrategy.class);

    @Override
    public void doAction(Long workspaceId, CredentialEntity entity, CloudbreakClient client) throws Exception {
        Log.log(" post "
                .concat(entity.getName())
                .concat(" private credential. "));
        try {
            entity.setResponse(
                    client.getCloudbreakClient()
                            .credentialV3Endpoint()
                            .createInWorkspace(workspaceId, entity.getRequest()));
        } catch (Exception e) {
            LOGGER.info("Creation of credential has failed, using existing if it is possible", e);
        }
    }
}
