package com.demo.IntegrationPlatform;

import org.apache.camel.spi.annotations.Component;
import org.springframework.beans.factory.annotation.Value;

@Component("myBean")
public class MySpringBean {

    @Value("${greeting}")
    private String say;

    public String saySomething() {
        return say;
    }

}