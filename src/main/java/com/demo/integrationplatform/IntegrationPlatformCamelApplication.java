package com.demo.integrationplatform;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.demo.integrationplatform")
@EnableCreateCacheAnnotation
public class IntegrationPlatformCamelApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationPlatformCamelApplication.class, args);
    }

}