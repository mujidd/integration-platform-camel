package com.demo.integrationplatform.restdsl;

import com.demo.integrationplatform.restdsl.types.PostRequestType;
import com.demo.integrationplatform.restdsl.types.ResponseType;
import io.micrometer.core.instrument.Tags;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.micrometer.MicrometerConstants;
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


        // Now define the REST API (POST, GET, etc.)
        rest()
                .path("/rest") // This makes the API available at http://host:port/$CONTEXT_ROOT/api

                .consumes("application/json")
                .produces("application/json")

                // HTTP: GET /rest
                .get()
                .outType(ResponseType.class) // Setting the response type enables Camel to marshal the response to JSON
                .to("bean:getBean") // This will invoke the Spring bean 'getBean'

                // HTTP: POST /rest
                .post()
                .type(PostRequestType.class) // Setting the request type enables Camel to unmarshal the request to a Java object
                .outType(ResponseType.class) // Setting the response type enables Camel to marshal the response to JSON
                .to("bean:postBean")

                // HTTP: GET /pets-service
                .get("/pets-service")
                .outType(ResponseType.class) // Setting the response type enables Camel to marshal the response to JSON
                .to("direct:call-pets-service");

        from("direct:call-pets-service")
                .onException(Exception.class).handled(true)
                .to("micrometer:counter:camel.api.counter?tags=Method=${header.CamelHttpMethod}=uri=${header.CamelHttpUri}=status=${header.CamelHttpResponseCode}")
                .to("log:afterProcess?showHeaders=true")
                .end()
                .log("test")
                .setHeader("id", simple("${random(1,3)}"))
                .to("rest:get:pets/{id}")
//                .throwException(new Exception())
                .convertBodyTo(String.class)
                .log("call-pets-service body: ${body}");
    }
}
