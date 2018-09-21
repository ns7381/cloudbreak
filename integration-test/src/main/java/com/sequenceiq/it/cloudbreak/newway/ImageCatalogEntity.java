package com.sequenceiq.it.cloudbreak.newway;

import com.sequenceiq.cloudbreak.api.model.imagecatalog.ImageCatalogRequest;
import com.sequenceiq.cloudbreak.api.model.imagecatalog.ImageCatalogResponse;
import com.sequenceiq.cloudbreak.api.model.imagecatalog.ImagesResponse;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;

public class ImageCatalogEntity extends AbstractCloudbreakEntity<ImageCatalogRequest, ImageCatalogResponse, ImageCatalog> {
    public static final String IMAGE_CATALOG = "IMAGE_CATALOG";

    public static final String IMAGE_CATALOG_URL = "IMAGE_CATALOG_URL";

    private ImagesResponse imagesResponse;

    ImageCatalogEntity(ImageCatalogRequest request, TestContext testContext) {
        super(request, testContext);
    }

    public ImageCatalogEntity(TestContext testContext) {
        super(new ImageCatalogRequest(), testContext);
    }

    ImageCatalogEntity() {
        super(IMAGE_CATALOG);
        setRequest(new ImageCatalogRequest());
    }

    public ImageCatalogEntity withName(String name) {
        getRequest().setName(name);
        setName(name);
        return this;
    }

    public ImageCatalogEntity withUrl(String url) {
        getRequest().setUrl(url);
        return this;
    }

    public ImagesResponse getResponseByProvider() {
        return imagesResponse;
    }

    public void setResponseByProvider(ImagesResponse imagesResponse) {
        this.imagesResponse = imagesResponse;
    }
}