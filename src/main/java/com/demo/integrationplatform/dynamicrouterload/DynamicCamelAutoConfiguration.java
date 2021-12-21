package com.demo.integrationplatform.dynamicrouterload;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.DefaultConfigurationConfigurer;
import org.apache.camel.model.Model;
import org.apache.camel.spi.BeanRepository;
import org.apache.camel.spi.StartupStepRecorder;
import org.apache.camel.spring.boot.*;
import org.apache.camel.spring.spi.ApplicationContextBeanRepository;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.support.startup.LoggingStartupStepRecorder;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.OrderComparator;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class DynamicCamelAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicCamelAutoConfiguration.class);

//    @Bean
//    @ConditionalOnMissingBean(DynamicCamelSpringBootApplicationListener.class)
    DynamicCamelSpringBootApplicationListener routesCollectorListener(ApplicationContext applicationContext, CamelConfigurationProperties config,
                                                               DynamicRoutesConfigurer dynamicRoutesConfigurer) {
        Collection<CamelContextConfiguration> configurations = applicationContext.getBeansOfType(CamelContextConfiguration.class).values();
        return new DynamicCamelSpringBootApplicationListener(applicationContext, new ArrayList(configurations), config, dynamicRoutesConfigurer);
    }

    // We explicitly declare the destroyMethod to be "" as the Spring @Bean
    // annotation defaults to AbstractBeanDefinition.INFER_METHOD otherwise
    // and in that case CamelContext::shutdown or CamelContext::stop would
    // be used for bean destruction. As SpringCamelContext is a lifecycle
    // bean (implements Lifecycle) additional invocations of shutdown or
    // close would be superfluous.
    @Bean(destroyMethod = "")
    @ConditionalOnMissingBean(CamelContext.class)
    CamelContext camelContext(ApplicationContext applicationContext,
                              CamelConfigurationProperties config,
                              DynamicPackageScanResourceResolver dynamicPackageScanResourceResolver) throws Exception {
        CamelContext camelContext = new SpringBootCamelContext(applicationContext, config.isWarnOnEarlyShutdown());
        return doConfigureCamelContext(applicationContext, camelContext, config,dynamicPackageScanResourceResolver);
    }

    /**
     * Not to be used by Camel end users
     */
    public static CamelContext doConfigureCamelContext(ApplicationContext applicationContext,
                                                       CamelContext camelContext,
                                                       CamelConfigurationProperties config,
                                                       DynamicPackageScanResourceResolver dynamicPackageScanResourceResolver) throws Exception {

        // setup startup recorder before building context
        configureStartupRecorder(camelContext, config);

        camelContext.build();

        // initialize properties component eager
        PropertiesComponent pc = applicationContext.getBeanProvider(PropertiesComponent.class).getIfAvailable();
        if (pc != null) {
            pc.setCamelContext(camelContext);
            camelContext.setPropertiesComponent(pc);
        }

        final Map<String, BeanRepository> repositories = applicationContext.getBeansOfType(BeanRepository.class);
        if (!repositories.isEmpty()) {
            List<BeanRepository> reps = new ArrayList<>();
            // include default bean repository as well
            reps.add(new ApplicationContextBeanRepository(applicationContext));
            // and then any custom
            reps.addAll(repositories.values());
            // sort by ordered
            OrderComparator.sort(reps);
            // and plugin as new registry
            camelContext.adapt(ExtendedCamelContext.class).setRegistry(new DefaultRegistry(reps));
        }

        if (ObjectHelper.isNotEmpty(config.getFileConfigurations())) {
            Environment env = applicationContext.getEnvironment();
            if (env instanceof ConfigurableEnvironment) {
                MutablePropertySources sources = ((ConfigurableEnvironment) env).getPropertySources();
                if (!sources.contains("camel-file-configuration")) {
                    sources.addFirst(new FilePropertySource("camel-file-configuration", applicationContext, config.getFileConfigurations()));
                }
            }
        }

        camelContext.adapt(ExtendedCamelContext.class).setPackageScanClassResolver(new FatJarPackageScanClassResolver());
        //change to dynamicPackageScanResourceResolver
        camelContext.adapt(ExtendedCamelContext.class).setPackageScanResourceResolver(dynamicPackageScanResourceResolver);

        if (config.getRouteFilterIncludePattern() != null || config.getRouteFilterExcludePattern() != null) {
            LOG.info("Route filtering pattern: include={}, exclude={}", config.getRouteFilterIncludePattern(), config.getRouteFilterExcludePattern());
            camelContext.getExtension(Model.class).setRouteFilterPattern(config.getRouteFilterIncludePattern(), config.getRouteFilterExcludePattern());
        }

        // configure the common/default options
        DefaultConfigurationConfigurer.configure(camelContext, config);
        // lookup and configure SPI beans
        DefaultConfigurationConfigurer.afterConfigure(camelContext);
        // and call after all properties are set
        DefaultConfigurationConfigurer.afterPropertiesSet(camelContext);

        return camelContext;
    }


    static void configureStartupRecorder(CamelContext camelContext, CamelConfigurationProperties config) {
        if ("false".equals(config.getStartupRecorder())) {
            camelContext.adapt(ExtendedCamelContext.class).getStartupStepRecorder().setEnabled(false);
        } else if ("logging".equals(config.getStartupRecorder())) {
            camelContext.adapt(ExtendedCamelContext.class).setStartupStepRecorder(new LoggingStartupStepRecorder());
        } else if ("java-flight-recorder".equals(config.getStartupRecorder())
                || config.getStartupRecorder() == null) {
            // try to auto discover camel-jfr to use
            StartupStepRecorder fr = camelContext.adapt(ExtendedCamelContext.class).getBootstrapFactoryFinder()
                    .newInstance(StartupStepRecorder.FACTORY, StartupStepRecorder.class).orElse(null);
            if (fr != null) {
                LOG.debug("Discovered startup recorder: {} from classpath", fr);
                fr.setRecording(config.isStartupRecorderRecording());
                fr.setStartupRecorderDuration(config.getStartupRecorderDuration());
                fr.setRecordingProfile(config.getStartupRecorderProfile());
                fr.setMaxDepth(config.getStartupRecorderMaxDepth());
                camelContext.adapt(ExtendedCamelContext.class).setStartupStepRecorder(fr);
            }
        }
    }
}
