package com.sequenceiq.it.cloudbreak.newway.action;

import static com.sequenceiq.it.cloudbreak.newway.log.Log.logJSON;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;

public class ImageCatalogCreateIfNotExistsAction implements ActionV2<ImageCatalog> {

    @Override
    public ImageCatalog action(TestContext testContext, ImageCatalog entity, CloudbreakClient client) throws Exception {
        try {
            entity.setResponse(
                    client.getCloudbreakClient().imageCatalogV3Endpoint().createInWorkspace(entity.workspaceId(), entity.getRequest())
            );
            logJSON("Imagecatalog post request: ", entity.getRequest());
        } catch (Exception e) {
            entity.setResponse(
                    client.getCloudbreakClient().imageCatalogV3Endpoint().getByNameInWorkspace(entity.workspaceId(), entity.getRequest().getName(), false));
        }
        if (entity.getResponse() == null) {
            throw new IllegalStateException("ImageCatalog cloudn not be created.");
        }
        return entity;
    }
}
