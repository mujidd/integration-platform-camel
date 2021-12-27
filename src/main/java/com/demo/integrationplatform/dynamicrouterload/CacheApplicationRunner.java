package com.demo.integrationplatform.dynamicrouterload;

import com.demo.integrationplatform.dynamicrouterload.service.jolt.DynamicDateMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CacheApplicationRunner implements ApplicationRunner {

    @Autowired
    private DynamicDateMappingService dynamicDateMappingService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dynamicDateMappingService.LoadAllDateMapping();
    }
}