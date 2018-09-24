package com.sequenceiq.it.cloudbreak.newway.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.sequenceiq.it.cloudbreak.newway.Mock.CLOUDBREAK_SERVER_ROOT;


import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.sequenceiq.cloudbreak.client.RestClientUtil;
import com.sequenceiq.it.cloudbreak.mock.json.CBVersion;
import com.sequenceiq.it.cloudbreak.newway.TestParameter;
import com.sequenceiq.it.spark.ITResponse;

@Service
public class ImageCatalogMockServerSetup {

    @Autowired
    private WireMockServer mockServer;

    public void configureImgCatalogMock(TestParameter testparams) {
        String jsonCatalogResponse = responseFromJsonFile("imagecatalog/catalog.json");
        String response = patchCbVersion(jsonCatalogResponse, testparams);
        mockServer.stubFor(get(urlEqualTo("/imagecatalog"))
                    .willReturn(aResponse().withStatus(200)
                    .withBody(response)));
    }


    public String getImgCatalogUrl(){
        return mockServer.baseUrl() + ITResponse.IMAGE_CATALOG;
    }

    public String patchCbVersion(String catalogJson, TestParameter testParameter) {
        return catalogJson.replace("CB_VERSION", getCloudbreakUnderTestVersion(testParameter.get(CLOUDBREAK_SERVER_ROOT)));
    }

    public static String responseFromJsonFile(String path) {
        try (InputStream inputStream = ITResponse.class.getResourceAsStream("/mockresponse/" + path)) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            return "";
        }
    }

    private String getCloudbreakUnderTestVersion(String cbServerAddress) {
        Client client = RestClientUtil.get();
        WebTarget target = client.target(cbServerAddress + "/info");
        CBVersion cbVersion = target.request().get().readEntity(CBVersion.class);
        return cbVersion.getApp().getVersion();
    }
}
