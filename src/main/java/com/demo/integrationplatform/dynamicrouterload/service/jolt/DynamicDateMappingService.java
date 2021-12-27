package com.demo.integrationplatform.dynamicrouterload.service.jolt;

public interface DynamicDateMappingService {

    void LoadAllDateMapping();

    void createDateMappingCache(String specName, String specValue);

    void deleteDateMappingCache(String specName);

    String selectDateMappingCache(String specName);
}
