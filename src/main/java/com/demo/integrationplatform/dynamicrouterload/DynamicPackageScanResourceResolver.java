package com.demo.integrationplatform.dynamicrouterload;

import org.apache.camel.impl.engine.DefaultPackageScanResourceResolver;
import org.apache.camel.spi.Resource;
import org.apache.camel.support.ResourceHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of the org.apache.camel.spi.PackageScanResourceResolver that is able to scan db to find resources.
 */
@Component("dynamicPackageScanResourceResolver")
public class DynamicPackageScanResourceResolver extends DefaultPackageScanResourceResolver {

    @Value("${routerDefinition}")
    private String routerDefinition;

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
        String routerDefinitionXml = "    <route id=\"dynamicRouter\">\n" +
                "      <from uri=\"timer:hello?period={{timer.period}}\"/>\n" +
                "      <transform>\n" +
                "        <method ref=\"myBean\" method=\"saySomething\"/>\n" +
                "      </transform>\n" +
                "      <filter>\n" +
                "        <simple>${body} contains 'foo'</simple>\n" +
                "        <to uri=\"log:foo\"/>\n" +
                "      </filter>\n" +
                "      <to uri=\"stream:out\"/>\n" +
                "    </route>";
        String routerDefinitionYml = "- route:\n" +
                "    # refer to the route configuration by the id to use for this route\n" +
                "    from: \"timer:yaml?period=10s\"\n" +
                "    steps:\n" +
                "      - set-body:\n" +
                "          simple: \"Timer fired ${header.CamelTimerCounter} times\"\n" +
                "      - to:\n" +
                "          uri: \"log:yaml\"\n" +
                "          parameters:\n" +
                "            show-body-type: false\n" +
                "            show-exchange-pattern: false\n" +
                "      - throw-exception:\n" +
                "          exception-type: \"java.lang.IllegalArgumentException\"\n" +
                "          message: \"Error from yaml\"";
        Resource resource1 = ResourceHelper.fromString("resource.xml", routerDefinitionXml);
        Resource resource2 = ResourceHelper.fromString("resource.yaml", routerDefinitionYml);
        answer.add(resource1);
        answer.add(resource2);
    }
}
