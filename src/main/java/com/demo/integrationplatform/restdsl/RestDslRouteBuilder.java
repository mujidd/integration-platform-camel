package com.demo.integrationplatform.restdsl;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

/**
 * This RouteBuilder defines our REST API using Camel's REST DSL.
 *
 * A RestConfiguration block first defines how the service will be instantiated.
 * The rest() DSL block then defines each of the RESTful service operations.
 */
@Component
public class RestDslRouteBuilder extends RouteBuilder {
    public void configure() throws Exception {

        // This section is required - it tells Camel how to configure the REST service
        restConfiguration()
                // Use the 'servlet' component.
                // This tells Camel to create and use a Servlet to 'host' the RESTful API.
                // Since we're using Spring Boot, the default servlet container is Tomcat.
                .component("servlet")
                // call the embedded rest service from the PetController
                .host("localhost").port(8080)
                // Allow Camel to try to marshal/unmarshal between Java objects and JSON
                .bindingMode(RestBindingMode.auto);
    }
}
