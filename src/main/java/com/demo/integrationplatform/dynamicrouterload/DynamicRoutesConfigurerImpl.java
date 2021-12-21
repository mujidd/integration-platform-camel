package com.demo.integrationplatform.dynamicrouterload;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.CamelBeanPostProcessor;
import org.apache.camel.spi.PackageScanResourceResolver;
import org.apache.camel.spi.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("dynamicRoutesConfigurer")
public class DynamicRoutesConfigurerImpl implements DynamicRoutesConfigurer{
    private CamelBeanPostProcessor beanPostProcessor;

    public CamelBeanPostProcessor getBeanPostProcessor() {
        return beanPostProcessor;
    }

    public void setBeanPostProcessor(CamelBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessor = beanPostProcessor;
    }
    @Override
    public void configureRoutes(CamelContext camelContext) throws Exception {
        if (getBeanPostProcessor() != null) {
            // lets use Camel's bean post processor on any existing route builder classes
            // so the instance has some support for dependency injection

            for (RoutesBuilder routeBuilder : collectDynamicRoutes(camelContext)) {
                getBeanPostProcessor().postProcessBeforeInitialization(routeBuilder, routeBuilder.getClass().getName());
                getBeanPostProcessor().postProcessAfterInitialization(routeBuilder, routeBuilder.getClass().getName());
            }
        }
    }

    public Collection<RoutesBuilder> collectDynamicRoutes(
            CamelContext camelContext) {

        final ExtendedCamelContext ecc = camelContext.adapt(ExtendedCamelContext.class);
        final PackageScanResourceResolver resolver = ecc.getPackageScanResourceResolver();
        final List<RoutesBuilder> answer = new ArrayList<>();
        try {
            for (Resource resource : getDynamicRoutes()) {
                Collection<RoutesBuilder> builders = ecc.getRoutesLoader().findRoutesBuilders(resource);
                if (builders.isEmpty()) {
                    continue;
                }
                answer.addAll(builders);
            }
        } catch (Exception e) {
            throw RuntimeCamelException.wrapRuntimeException(e);
        }

        return answer;
    }

    private Collection<Resource> getDynamicRoutes() {
        //TODO
        return new ArrayList<>();
    }
}
