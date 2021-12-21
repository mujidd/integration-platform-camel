package com.demo.integrationplatform.dynamicrouterload;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.main.RoutesConfigurer;
import org.apache.camel.spring.boot.CamelConfigurationProperties;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.spring.boot.CamelSpringBootInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;


/**
 * A spring application listener that when spring boot is starting (refresh event) will setup Dynamic Camel Routers:
 * <p>
 * 1. collecting routes and rests from the DB(like SQL or mongodb) and injects these into the Camel context.
 */
public class DynamicCamelSpringBootApplicationListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private final ApplicationContext applicationContext;
    private final List<CamelContextConfiguration> camelContextConfigurations;
    private final CamelConfigurationProperties configurationProperties;
    private final DynamicRoutesConfigurer dynamicRoutesConfigurer;

    // Constructors
    public DynamicCamelSpringBootApplicationListener(ApplicationContext applicationContext, List<CamelContextConfiguration> camelContextConfigurations,
                                                     CamelConfigurationProperties configurationProperties,
                                                     DynamicRoutesConfigurer dynamicRoutesConfigurer) {
        this.applicationContext = applicationContext;
        this.camelContextConfigurations = new ArrayList<>(camelContextConfigurations);
        this.configurationProperties = configurationProperties;
        this.dynamicRoutesConfigurer = dynamicRoutesConfigurer;
    }

    // Overridden
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        CamelContext camelContext = applicationContext.getBean(CamelContext.class);

        // only add and start Camel if its stopped (initial state)
        if (event.getApplicationContext() == this.applicationContext
                && camelContext.getStatus().isStopped()) {

            try {
                // we can use the default routes configurer
                RoutesConfigurer configurer = new RoutesConfigurer();

                if (configurationProperties.isRoutesCollectorEnabled()) {
                    configurer.setBeanPostProcessor(camelContext.adapt(ExtendedCamelContext.class).getBeanPostProcessor());
                    configurer.configureRoutes(camelContext);
                }
            } catch (Exception e) {
                throw new CamelSpringBootInitializationException(e);
            }
        }
    }

    @Override
    public int getOrder() {
        // 1. DynamicRoutesCollector (LOWEST_PRECEDENCE - 3)
        // 1. RoutesCollector (LOWEST_PRECEDENCE - 2)
        // 2. CamelContextFactoryBean (LOWEST_PRECEDENCE -1)
        // 3. SpringCamelContext (LOWEST_PRECEDENCE)
        return LOWEST_PRECEDENCE - 3;
    }
}


