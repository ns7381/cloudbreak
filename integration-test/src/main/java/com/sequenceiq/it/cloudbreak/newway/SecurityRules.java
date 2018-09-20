package com.sequenceiq.it.cloudbreak.newway;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.sequenceiq.it.IntegrationTestContext;

public class SecurityRules extends SecurityRulesEntity {

    static Function<IntegrationTestContext, SecurityRules> getTestContext(String key) {
        return testContext -> testContext.getContextParam(key, SecurityRules.class);
    }

    static Function<IntegrationTestContext, SecurityRules> getNew() {
        return testContext -> new SecurityRules();
    }

    public static SecurityRules request() {
        return new SecurityRules();
    }

    public static List<SecurityRules> valid() {
        return List.of(request()
                .withSubnet("0.0.0.0/0")
                .withProtocol("tcp")
                .withPorts("22,443,8443,9443,8080"));
    }

    public static Action<SecurityRules> getDefaultSecurityRules(String key) {
        return new Action<>(getTestContext(key), SecurityRulesAction::getDefaultSecurityRules);
    }

    public static Action<SecurityRules> getDefaultSecurityRules() {
        return getDefaultSecurityRules(SECURITYRULES);
    }

    public static Assertion<SecurityRules> assertThis(BiConsumer<SecurityRules, IntegrationTestContext> check) {
        return new Assertion<>(getTestContext(GherkinTest.RESULT), check);
    }

    public SecurityRules withSubnet(String subnet) {
        getRequest().setSubnet(subnet);
        return this;
    }

    public SecurityRules withProtocol(String protocol) {
        getRequest().setProtocol(protocol);
        return this;
    }

    public SecurityRules withPorts(String ports) {
        getRequest().setPorts(ports);
        return this;
    }
}
