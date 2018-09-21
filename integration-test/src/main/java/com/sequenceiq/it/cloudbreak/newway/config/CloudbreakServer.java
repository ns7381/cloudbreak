package com.sequenceiq.it.cloudbreak.newway.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.TestParameter;

@Component
public class CloudbreakServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudbreakServer.class);

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
    private TestParameter testParameter;

    @PostConstruct
    private void init() {
        testParameter.put(CloudbreakTest.CLOUDBREAK_SERVER_ROOT, server + cbRootContextPath);
        testParameter.put(CloudbreakTest.IDENTITY_URL, uaaServer);
        testParameter.put(CloudbreakTest.USER, defaultUaaUser);
        testParameter.put(CloudbreakTest.PASSWORD, defaultUaaPassword);
    }
}
