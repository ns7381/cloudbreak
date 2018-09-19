package com.sequenceiq.it.cloudbreak.newway.ger;

import static com.sequenceiq.it.cloudbreak.newway.log.Log.logJSON;

import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.Entity;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalogEntity;

public class ImageCatalogCreateStrategy implements StrategyV2 {
    @Override
    public void doAction(IntegrationTestContext integrationTestContext, Entity entity, CloudbreakClient client) throws Exception {
        ImageCatalogEntity imageCatalogEntity = (ImageCatalogEntity) entity;
//        String imageCatalogUrl = integrationTestContext.getContextParam(IMAGE_CATALOG_URL, String.class);
        Long workspaceId = integrationTestContext.getContextParam(CloudbreakTest.WORKSPACE_ID, Long.class);

//        if (imageCatalogUrl != null && imageCatalogEntity.getRequest().getUrl() == null) {
//            imageCatalogEntity.getRequest().setUrl(imageCatalogUrl);
//        }

        imageCatalogEntity.setResponse(
                client.getCloudbreakClient()
                        .imageCatalogV3Endpoint().createInWorkspace(workspaceId, imageCatalogEntity.getRequest()));
        logJSON("Imagecatalog post request: ", imageCatalogEntity.getRequest());
    }

    @Override
    public void doAction(IntegrationTestContext integrationTestContext, Entity entity) throws Exception {

    }
}
