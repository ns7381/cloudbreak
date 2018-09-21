package com.sequenceiq.it.cloudbreak.newway.action;

import static com.sequenceiq.it.cloudbreak.newway.log.Log.logJSON;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;

public class ImageCatalogCreateAction implements ActionV2<ImageCatalog> {

    @Override
    public ImageCatalog action(TestContext testContext, ImageCatalog entity, CloudbreakClient client) throws Exception {
        entity.setResponse(
                client.getCloudbreakClient().imageCatalogV3Endpoint().createInWorkspace(entity.workspaceId(), entity.getRequest())
        );
        logJSON("Imagecatalog post request: ", entity.getRequest());
        return entity;
    }
}
