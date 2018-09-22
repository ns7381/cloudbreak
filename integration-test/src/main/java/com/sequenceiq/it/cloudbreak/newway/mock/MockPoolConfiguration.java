package com.sequenceiq.it.cloudbreak.newway.mock;


import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.util.concurrent.ThreadLocalRandom;

import org.mockserver.integration.ClientAndServer;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import com.sequenceiq.it.cloudbreak.newway.config.SparkServer;

@Configuration
public class MockPoolConfiguration {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public SparkServer sparkMockEntity() {
        //get random range of ports
        int randomPort = ThreadLocalRandom.current().nextInt(9400, 9900 + 1);
        return new SparkServer("localhost", randomPort);
    }


    @Bean
    public ThreadLocalTargetSource sparkInstancePool(){
        ThreadLocalTargetSource pool = new ThreadLocalTargetSource();
        pool.setTargetBeanName("sparkMockEntity");
        return pool;
    }

    @Bean
    @Primary
    public ProxyFactoryBean pooledSparkEntity(){
        ProxyFactoryBean proxyFactory = new ProxyFactoryBean();
        proxyFactory.setTargetSource(sparkInstancePool());
        return proxyFactory;
    }


    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ClientAndServer mockServer() {
        //get random range of ports
        int randomPort = ThreadLocalRandom.current().nextInt(8400, 8900 + 1);
        ClientAndServer server = ClientAndServer.startClientAndServer("localhost", randomPort);
        return server;
    }


//    @Bean
//    public ThreadLocalTargetSource mockServerInstancePool(){
//        ThreadLocalTargetSource pool = new ThreadLocalTargetSource();
//        pool.setTargetBeanName("mockServer");
//        return pool;
//    }
//
//    @Bean
//    @Primary
//    public ProxyFactoryBean pooledMockServerEntity(){
//        ProxyFactoryBean proxyFactory = new ProxyFactoryBean();
//        proxyFactory.setTargetSource(mockServerInstancePool());
//        return proxyFactory;
//    }


}
