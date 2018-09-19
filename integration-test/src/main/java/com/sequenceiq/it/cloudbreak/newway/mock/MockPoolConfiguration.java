package com.sequenceiq.it.cloudbreak.newway.mock;


import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.sequenceiq.it.cloudbreak.newway.SparkMockEntity;

@Configuration
public class MockPoolConfiguration {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public SparkMockEntity sparkMockEntity() {
        return new SparkMockEntity();
    }


    @Bean
    public ThreadLocalTargetSource sparkInstancePool(){
        ThreadLocalTargetSource pool = new ThreadLocalTargetSource();
        pool.setTargetBeanName("sparkMockEntity");
        return pool;
    }

    @Bean
    public ProxyFactoryBean pooledSparkEntity(){
        ProxyFactoryBean proxyFactory = new ProxyFactoryBean();
        proxyFactory.setTargetSource(sparkInstancePool());
        return proxyFactory;
    }

}
