package com.demo.integrationplatform.dynamicrouterload.service.schema;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CreateCache;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSchemaEntity;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSchemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class DynamicValidatorServiceImpl implements DynamicValidatorService {
    
    private static final Logger LOG = LoggerFactory.getLogger(DynamicValidatorServiceImpl.class);

    @Autowired
    private JsSchemaRepository jsSchemaRepository;

    @CreateCache(name = "jsSchemaCache")
    @CacheRefresh(refresh = 30, timeUnit = TimeUnit.MINUTES)
    @CachePenetrationProtect
    private Cache<String, String> jsSchemaCache;

    @Override
    public void init() {
        LoadAllValidator();
        jsSchemaCache.config().setLoader(this::loadValidatorFromDB);
    }

    private String loadValidatorFromDB(String title) {
        try {
            return jsSchemaRepository.findBySchemaTitle(title).stream().findFirst().get().getSchemaContent();
        }
        catch (Exception e){
            LOG.error("Refresh date mapping failed : {}", title);
            return "";
        }
    }

    @Override
    public void LoadAllValidator() {
        List<JsSchemaEntity> jsSchemaEntities = jsSchemaRepository.findAll();
        if(CollectionUtils.isEmpty(jsSchemaEntities)){
            return;
        }
        for(JsSchemaEntity jsSchemaEntity : jsSchemaEntities)
        {
            jsSchemaCache.PUT(jsSchemaEntity.getSchemaTitle(),jsSchemaEntity.getSchemaContent());
        }
        LOG.info("load dynamic jsSchema completed");
    }

    @Override
    public void createValidatorCache(String schemaName, String schemaValue) {
        jsSchemaCache.put(schemaName,schemaValue);
    }

    @Override
    public void deleteValidatorCache(String schemaName) {
        jsSchemaCache.remove(schemaName);
    }

    @Override
    public String selectValidatorCache(String schemaName) {
        return jsSchemaCache.get(schemaName);
    }
}
