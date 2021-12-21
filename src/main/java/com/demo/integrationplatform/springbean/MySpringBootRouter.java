package com.demo.integrationplatform.springbean;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {
//        from("timer:hello?period=1000").routeId("hello")
//                        .transform().method("myBean", "saySomething")
//                        .filter(simple("${body} contains 'foo'"))
//                        .to("log:foo")
//                        .end()
//                        .to("stream:out").onException(Exception.class);
    }

}
