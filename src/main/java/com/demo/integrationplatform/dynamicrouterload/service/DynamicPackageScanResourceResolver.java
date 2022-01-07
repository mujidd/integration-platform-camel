package com.demo.integrationplatform.dynamicrouterload.service;

import com.demo.integrationplatform.dynamicrouterload.DynamicCamelAutoConfiguration;
import com.demo.integrationplatform.dynamicrouterload.entity.ProcessEntity;
import com.demo.integrationplatform.dynamicrouterload.entity.ProcessRepository;
import com.demo.integrationplatform.dynamicrouterload.entity.RouterEntity;
import com.demo.integrationplatform.dynamicrouterload.entity.RouterRepository;
import org.apache.camel.impl.engine.DefaultPackageScanResourceResolver;
import org.apache.camel.spi.Resource;
import org.apache.camel.support.ResourceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An implementation of the org.apache.camel.spi.PackageScanResourceResolver that is able to scan db to find resources.
 */
@Component("dynamicPackageScanResourceResolver")
public class DynamicPackageScanResourceResolver extends DefaultPackageScanResourceResolver {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicPackageScanResourceResolver.class);

    @Autowired
    private RouterRepository routerRepository;

    @Autowired
    private ProcessRepository processRepository;


    @Override
    public Collection<Resource> findResources(String location) throws Exception {
        Set<Resource> answer = new HashSet<>();
        if(isDynamicResource(location))
        {
            doFindDynamicResources(location, answer);
            return answer;
        }
        doFindResources(location, answer);
        return answer;
    }

    private boolean isDynamicResource(String uri) {
        if (uri == null) {
            return false;
        }
        String scheme = uri.substring(0, uri.indexOf(':') + 1);
        return ("dynamicConfig:".equals(scheme));
    }

    private void doFindDynamicResources(String location, Set<Resource> resources) {
        LoadProcess(resources);
        LoadRouters(resources);
        LOG.info("load dynamic router and process completed & count is {}", resources.size());
    }

    private void LoadProcess(Set<Resource> resources) {
        List<ProcessEntity> processEntities = processRepository.findAllByStatus("s");
        if(CollectionUtils.isEmpty(processEntities)){
            return;
        }
        for(ProcessEntity processEntity : processEntities)
        {
            try
            {
                String processDefinitionXml =processEntity.getProcessFlow();
                String processName =processEntity.getProcessName();
                Resource resource = ResourceHelper.fromString(processName+"process.xml", processDefinitionXml);
                resources.add(resource);
            }
            catch (Exception e)
            {
                LOG.error("load dynamic process failed: {}", processEntity);
            }
        }
    }

    private void LoadRouters(Set<Resource> resources) {
        List<RouterEntity> routerEntityList = routerRepository.findAllByStatus("s");
        if(CollectionUtils.isEmpty(routerEntityList)){
            return;
        }
        for(RouterEntity routerEntity : routerEntityList)
        {
            try
            {
                String routerDefinitionXml =routerEntity.getRouterFlow();
                String routerName =routerEntity.getRouterName();
                Resource resource = ResourceHelper.fromString(routerName+"router.xml", routerDefinitionXml);
                resources.add(resource);
            }
            catch (Exception e)
            {
                LOG.error("load dynamic router failed: {}", routerEntity);
            }

        }
    }
}
