package com.demo.integrationplatform.dynamicrouterload.service.jolt;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSpecEntity;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSpecRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class DynamicDateMappingServiceImpl implements DynamicDateMappingService{
    
    private static final Logger LOG = LoggerFactory.getLogger(DynamicDateMappingServiceImpl.class);

    @Autowired
    private JsSpecRepository jsSpecRepository;

    @CreateCache(name = "dateMappingCache")
    @CacheRefresh(refresh = 30, timeUnit = TimeUnit.MINUTES)
    @CachePenetrationProtect
    private Cache<String, String> dateMappingCache;

    @Override
    public void init(){
        LoadAllDateMapping();
        dateMappingCache.config().setLoader(this::loadDateMappingFromDB);
    }

    private String loadDateMappingFromDB(String specTitle) {
        try {
            return jsSpecRepository.findBySpecTitle(specTitle).stream().findFirst().get().getJoltSpec();
        }
        catch (Exception e){
            LOG.error("Refresh date mapping failed : {}", specTitle);
            return "";
        }
    }

    @Override
    public void LoadAllDateMapping() {
        List<JsSpecEntity> JsSpecEntities = jsSpecRepository.findAll();
        if(CollectionUtils.isEmpty(JsSpecEntities)){
            return;
        }
        for(JsSpecEntity jsSpecEntity : JsSpecEntities)
        {
            dateMappingCache.PUT(jsSpecEntity.getSpecTitle(),jsSpecEntity.getJoltSpec());
        }
        LOG.info("load dynamic JsSpec completed");
    }

    @Override
    public void createDateMappingCache(String specName, String specValue) {
        dateMappingCache.PUT(specName,specValue);
    }

    @Override
    public void deleteDateMappingCache(String specName) {
        dateMappingCache.remove(specName);
    }

    @Override
    public String selectDateMappingCache(String specName) {
        return dateMappingCache.get(specName);
    }
}
