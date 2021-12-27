package com.demo.integrationplatform.dynamicrouterload.service.jolt;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.demo.integrationplatform.dynamicrouterload.constant.Define;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSpecEntity;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSpecRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("dynaConf")
public class DynamicDataMapping {

    @Autowired
    private JsSpecRepository jsSpecRepository;

    @Autowired
    private DynamicDateMappingService dynamicDateMappingService;

    private ObjectMapper jsonUtil = new ObjectMapper();

    private static List<String>HEADER_MAP_COPY_BLACK_LIST =
            Lists.newArrayList("CamelHttpServletRequest", "CamelHttpServletResponse");

    public String process(Long specId, Exchange exchange) throws Exception {
        // get Body & Headers
        Message msg = exchange.getMessage();
        String transformedJson = null;
        if (null != msg) {
            Map<String, Object> headers = msg.getHeaders();
            if (null != headers) {
                Map<String, Object>headerMap = new HashMap<>();
                headers.forEach((key, value) -> {
                    if(!HEADER_MAP_COPY_BLACK_LIST.contains(key)) {
                        headerMap.put(key, value);
                    }
                });

                Map<String, Object> bodyMap = null;
                String httpMethod = (String) headerMap.get("CamelHttpMethod");
                if (StringUtils.containsAny(httpMethod, HttpMethod.POST.name(), HttpMethod.PUT.name())) {
                    Object body = msg.getBody();
                    if (null != body) {
                        // put BodyMap & HeadersMap into AllMap
//                        bodyMap = jsonUtil.readValue((String) body, Map.class);
                        bodyMap = (Map)body;
                    }
                }// END -> Charge POST PUT
                else {
                    bodyMap = new HashMap<>();
                }

                Map<String, Object> allInOneMap = new HashMap<>();
                allInOneMap.put(Define.DYNA_CONF_PARAM.header.name(), headerMap);
                if(null != bodyMap && 0 != bodyMap.size()) {
                    allInOneMap.put(Define.DYNA_CONF_PARAM.body.name(), bodyMap);
                }

                // json will be got from DB
                List<Object> specList = getSpecsByName("spec_test");
                transformedJson = doTransform(allInOneMap, specList);

//                msg.setBody(transformedJson);
            }
        }
        return transformedJson;
    }

    private List<Object> getSpecsByName(String name) {
        String specJsonContents = dynamicDateMappingService.selectDateMappingCache(name);
        return JsonUtils.jsonToList(specJsonContents);
    }

    private String doTransform(Object input, Object spec) {
        Chainr chainr = Chainr.fromSpec(spec);
        Object output = chainr.transform(input);
        String retStr = JsonUtils.toJsonString(((Map)output).get(Define.DYNA_CONF_PARAM.body.name()));

        return retStr;
    }
}
