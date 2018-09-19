package com.sequenceiq.it.cloudbreak.newway.ger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;

@Component
public class CloudbreakServer {

    @Value("${integrationtest.cloudbreak.server}")
    private String server;

    @Value("${server.contextPath:/cb}")
    private String cbRootContextPath;

    @Value("${integrationtest.uaa.server}")
    private String uaaServer;

    @Value("${integrationtest.uaa.user}")
    private String defaultUaaUser;

    @Value("${integrationtest.uaa.password}")
    private String defaultUaaPassword;

    @Inject
    private IntegrationTestContext integrationTestContext;

    @PostConstruct
    private void ps() {
        integrationTestContext.putContextParam(CloudbreakTest.CLOUDBREAK_SERVER_ROOT, server + cbRootContextPath);
        integrationTestContext.putContextParam(CloudbreakTest.IDENTITY_URL, uaaServer);
        integrationTestContext.putContextParam(CloudbreakTest.USER, defaultUaaUser);
        integrationTestContext.putContextParam(CloudbreakTest.PASSWORD, defaultUaaPassword);
    }
}
