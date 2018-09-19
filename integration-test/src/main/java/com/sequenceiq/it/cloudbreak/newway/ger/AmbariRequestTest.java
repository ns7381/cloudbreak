package com.sequenceiq.it.cloudbreak.newway.ger;

import com.sequenceiq.cloudbreak.api.model.AmbariStackDetailsJson;
import com.sequenceiq.cloudbreak.api.model.v2.AmbariV2Request;

public class AmbariRequestTest {

    private AmbariV2Request request;

    public AmbariRequestTest(AmbariV2Request req) {
        request = req;
    }

    public static AmbariV2Request request() {
        return new AmbariV2Request();
    }

    public static AmbariRequestTest def() {
        var req = new AmbariV2Request();
        req.setUserName("admin");
        req.setPassword("admin1234");
        req.setBlueprintName("blueprint-name");
        req.setValidateBlueprint(false);
        req.setValidateRepositories(Boolean.TRUE);
        req.setAmbariStackDetails(new AmbariStackDetailsJson());
        return new AmbariRequestTest(req);
    }

    public AmbariRequestTest withName(String name) {
        request.setBlueprintName(name);
        return this;
    }

    public AmbariV2Request build() {
        return request;
    }
}
