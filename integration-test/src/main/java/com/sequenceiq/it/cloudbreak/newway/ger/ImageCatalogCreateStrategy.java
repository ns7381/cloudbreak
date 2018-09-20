package com.sequenceiq.it.cloudbreak.newway.ger;

import static com.sequenceiq.it.cloudbreak.newway.log.Log.logJSON;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;

public class ImageCatalogCreateStrategy implements StrategyV2<ImageCatalog> {

    @Override
    public void doAction(Long workspaceId, ImageCatalog entity, CloudbreakClient client) throws Exception {
        entity.setResponse(
                client.getCloudbreakClient().imageCatalogV3Endpoint().createInWorkspace(workspaceId, entity.getRequest())
        );
        logJSON("Imagecatalog post request: ", entity.getRequest());
    }
}
