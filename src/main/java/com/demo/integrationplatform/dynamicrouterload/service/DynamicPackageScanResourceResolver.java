package com.demo.integrationplatform.dynamicrouterload.service;

import com.demo.integrationplatform.dynamicrouterload.entity.RouterEntity;
import com.demo.integrationplatform.dynamicrouterload.entity.RouterRepository;
import org.apache.camel.impl.engine.DefaultPackageScanResourceResolver;
import org.apache.camel.spi.Resource;
import org.apache.camel.support.ResourceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${routerDefinition}")
    private String routerDefinition;

    @Autowired
    private RouterRepository routerRepository;

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

    private void doFindDynamicResources(String location, Set<Resource> answer) {
        List<RouterEntity> routerEntityList = routerRepository.findAll();
        if(CollectionUtils.isEmpty(routerEntityList)){
            return;
        }
        for(RouterEntity routerEntity : routerEntityList)
        {
            String routerDefinitionXml =routerEntity.getRouterFlow();
            String routerName =routerEntity.getRouterName();
            Resource resource = ResourceHelper.fromString(routerName+".xml", routerDefinitionXml);
            answer.add(resource);
        }
//        String routerDefinitionXml = "    <route id=\"dynamicRouter\">\n" +
//                "      <from uri=\"timer:hello?period={{timer.period}}\"/>\n" +
//                "      <transform>\n" +
//                "        <method ref=\"myBean\" method=\"saySomething\"/>\n" +
//                "      </transform>\n" +
//                "      <filter>\n" +
//                "        <simple>${body} contains 'foo'</simple>\n" +
//                "        <to uri=\"log:foo\"/>\n" +
//                "      </filter>\n" +
//                "      <to uri=\"stream:out\"/>\n" +
//                "    </route>";
//        Resource resource1 = ResourceHelper.fromString("resource.xml", routerDefinitionXml);
//        answer.add(resource1);
    }
}
