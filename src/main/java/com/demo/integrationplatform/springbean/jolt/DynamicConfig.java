package com.demo.integrationplatform.springbean.jolt;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.demo.integrationplatform.dynamicrouterload.constant.Define;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSpectEntity;
import com.demo.integrationplatform.dynamicrouterload.entity.JsSpectRepository;
import com.demo.integrationplatform.dynamicrouterload.entity.RouterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("dynaConf")
public class DynamicConfig {

    @Autowired
    private JsSpectRepository jsSpectRepository;

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
                List<Object> specList = getSpectsById(specId);
                transformedJson = doTransform(allInOneMap, specList);

//                msg.setBody(transformedJson);
            }
        }
        return transformedJson;
    }

    private List<Object> getSpectsById(Long specId) {
        //                List<Object> specList = getSpectsById(specId);
        //                String specJson = "/demo_spec.json";
        //                List<Object> specList = JsonUtils.classpathToList(specJson);
        //TODO cache spec or load spec when init
        JsSpectEntity jsSpectEntity = jsSpectRepository.findById(specId).get();
        String specJsonContents = jsSpectEntity.getJoltSpec();
        return JsonUtils.jsonToList(specJsonContents);
    }

    private String doTransform(Object input, Object spec) {
        Chainr chainr = Chainr.fromSpec(spec);
        Object output = chainr.transform(input);
        String retStr = JsonUtils.toJsonString(((Map)output).get(Define.DYNA_CONF_PARAM.body.name()));

        return retStr;
    }
}
