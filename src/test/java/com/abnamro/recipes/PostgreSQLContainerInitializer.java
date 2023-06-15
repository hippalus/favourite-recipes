package com.abnamro.recipes;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final PostgreSQLContainer<?> postgreSQLContainer = this.postgresContainer();
        postgreSQLContainer.start();

        TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
        ).applyTo(applicationContext.getEnvironment());

        applicationContext.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> postgreSQLContainer.close());
    }


    private PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:latest");
    }

}
