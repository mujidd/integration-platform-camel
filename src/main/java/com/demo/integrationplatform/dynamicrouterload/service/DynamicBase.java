package com.demo.integrationplatform.dynamicrouterload.service;

import com.demo.integrationplatform.dynamicrouterload.constant.Define;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.camel.Message;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DynamicBase {

    private static List<String> HEADER_MAP_COPY_BLACK_LIST =
            Lists.newArrayList("CamelHttpServletRequest", "CamelHttpServletResponse");

    public final static String SCHEMA_VAR_FLAG = "V7";

    protected final ObjectMapper jsonUtil = new ObjectMapper();

    protected Map<String, Object>getHeaderBodyMap(Message msg) throws IOException {
        Map<String, Object> allInOneMap = new HashMap<>();

        Map<String, Object> headers = msg.getHeaders();
        if (null != headers) {
            Map<String, Object> headerMap = new HashMap<>();
            headers.forEach((key, value) -> {
                if (!HEADER_MAP_COPY_BLACK_LIST.contains(key)) {
                    headerMap.put(key, value);
                }
            });

            Object bodyObj = msg.getBody();
            String httpMethod = (String) headerMap.get("CamelHttpMethod");
            Map<String, Object> bodyMap = fetchBodyMap(bodyObj, httpMethod);

            allInOneMap.put(Define.DYNA_PARAM_CONSTRUCT.header.name(), headerMap);
            if (null != bodyMap && 0 != bodyMap.size()) {
                allInOneMap.put(Define.DYNA_PARAM_CONSTRUCT.body.name(), bodyMap);
            }
        }

        return allInOneMap;
    }

    private Map<String, Object> fetchBodyMap(Object bodyObj, String httpMethod) throws IOException {
        Map<String, Object> bodyMap = null;
        if (StringUtils.containsAny(httpMethod, HttpMethod.POST.name(), HttpMethod.PUT.name())) {
            if (null != bodyObj) {
                // put BodyMap & HeadersMap into AllMap
                if (bodyObj instanceof Map) {
                    bodyMap = (Map) bodyObj;
                } else if (bodyObj instanceof InputStream) {
                    InputStream bodyIn = (InputStream) bodyObj;
                    byte[]bodyByArr = new byte[bodyIn.available()];
                    IOUtils.readFully(bodyIn, bodyByArr);
                    IOUtils.closeQuietly(bodyIn);
                    bodyMap = jsonUtil.readValue(bodyByArr, Map.class);
                } else {
                    bodyMap = jsonUtil.readValue((String) bodyObj, Map.class);
                }
            }
        }// END -> Charge POST PUT
        else {
            // HttpRequestParamters are stored in Headers(Map)
            bodyMap = new HashMap<>();
        }
        return bodyMap;
    }
}
