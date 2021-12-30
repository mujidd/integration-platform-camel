package com.demo.integrationplatform.dynamicrouterload.service.jolt;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.demo.integrationplatform.dynamicrouterload.constant.Define;
import com.demo.integrationplatform.dynamicrouterload.service.DynamicBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("dynaConf")
public class DynamicDataMapping extends DynamicBase {

    @Autowired
    private DynamicDateMappingService dynamicDateMappingService;

    private ObjectMapper jsonUtil = new ObjectMapper();

    private static List<String>HEADER_MAP_COPY_BLACK_LIST =
            Lists.newArrayList("CamelHttpServletRequest", "CamelHttpServletResponse");

    public String process(String specName, Exchange exchange) throws Exception {
        // get Body & Headers
        Message msg = exchange.getMessage();
        String transformedJson = null;
        if (null != msg) {
            Map<String, Object> allInOneMap = super.getHeaderBodyMap(msg);
            if (MapUtils.isNotEmpty(allInOneMap)) {
                List<Object> specList = getSpecsByName(specName);
                transformedJson = doTransform(allInOneMap, specList);
            }
        }
        return transformedJson;
    }

    private String doTransform(Object input, Object spec) {
        Chainr chainr = Chainr.fromSpec(spec);
        Object output = chainr.transform(input);
        String retStr = JsonUtils.toJsonString(
                ((Map) output).get(Define.DYNA_PARAM_CONSTRUCT.body.name()));

        return retStr;
    }

    private List<Object> getSpecsByName(String name) {
        String specJsonContents = dynamicDateMappingService.selectDateMappingCache(name);
        return JsonUtils.jsonToList(specJsonContents);
    }
}
