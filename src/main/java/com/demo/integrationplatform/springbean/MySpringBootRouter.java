package com.demo.integrationplatform.springbean;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class MySpringBootRouter extends RouteBuilder {
    @Override
    public void configure() {
        restConfiguration()
                // Use the 'servlet' component.
                // This tells Camel to create and use a Servlet to 'host' the RESTful API.
                .component("servlet")
                .enableCORS(false)
                .host("localhost").port(8080)
                // Allow Camel to try to marshal/unmarshal between Java objects and JSON
                .bindingMode(RestBindingMode.auto);
    }
}
