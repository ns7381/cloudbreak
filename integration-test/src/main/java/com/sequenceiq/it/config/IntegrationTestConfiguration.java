package com.sequenceiq.it.config;

import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.testng.TestNG;

import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.SuiteContext;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;

@Configuration
@ComponentScan("com.sequenceiq.it")
@EnableConfigurationProperties
public class IntegrationTestConfiguration {
    @Bean
    public static PropertyResourceConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public SuiteContext suiteContext() {
        return new SuiteContext();
    }

    @Bean
    public TestNG testNG() {
        return new TestNG();
    }

    @Bean
    public IntegrationTestContext integrationTestContext() {
        return new IntegrationTestContext();
    }

    @Bean
    public CloudbreakClient cloudbreakClient(IntegrationTestContext integrationTestContext) throws Exception {
        CloudbreakClient cloudbreakClient = com.sequenceiq.it.cloudbreak.newway.CloudbreakClient.isCreated();
        cloudbreakClient.create(integrationTestContext, cloudbreakClient);
        return cloudbreakClient;
    }
}
