package com.demo.integrationplatform.dynamicrouterload.service;

import org.apache.camel.CamelContext;

public interface DynamicRoutesConfigurer {
    void configureRoutes(CamelContext camelContext) throws Exception;
}
