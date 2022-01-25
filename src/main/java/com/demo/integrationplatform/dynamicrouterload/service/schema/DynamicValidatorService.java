package com.demo.integrationplatform.dynamicrouterload.service.schema;

public interface DynamicValidatorService {
    void init();

    void LoadAllValidator();

    void createValidatorCache(String schemaName, String schemaValue);

    void deleteValidatorCache(String schemaName);

    String selectValidatorCache(String schemaName);
}
