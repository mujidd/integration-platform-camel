package com.demo.integrationplatform.springbean;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class MySpringBootRouter extends RouteBuilder {
    final String saslJaasConfig = "RAW(org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"Endpoint=sb://ivislevents.servicebus.chinacloudapi.cn/;SharedAccessKeyName=test;SharedAccessKey=ZiA2sJN2Q4TJ1JHAxBRUchjm+NLAZCi+i+xjMXUJHK8=;EntityPath=loginevents\";)";
    final String brokers = "ivislevents.servicebus.chinacloudapi.cn:9093";
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
//        rest()
//                .path("/v1/d/is-eventhub/loginStatus") // This makes the API available at http://host:port/$CONTEXT_ROOT/api
//
//                .consumes("application/json")
//                .produces("application/json")
//
//                // HTTP: POST /rest
//                .post()
//                .to("direct:eventhub");

//        from("timer:hello?period=5000").routeId("camel")
//                .setBody(constant("Message from Camel"))          // Message to send
//                .setHeader(KafkaConstants.KEY, constant("Camel")) // Key of the message
//                .to("kafka:loginevents?"
//                        + "brokers=" + brokers
//                        + "&saslMechanism=PLAIN"
//                        + "&securityProtocol=SASL_SSL"
//                        + "&saslJaasConfig=" + saslJaasConfig);
        from("kafka:loginevents?"
                + "brokers=" + brokers
                + "&saslMechanism=PLAIN"
                + "&securityProtocol=SASL_SSL"
                + "&saslJaasConfig=" + saslJaasConfig)
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}");
    }
}
