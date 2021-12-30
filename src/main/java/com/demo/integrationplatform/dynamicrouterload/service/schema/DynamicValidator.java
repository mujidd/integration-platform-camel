package com.demo.integrationplatform.dynamicrouterload.service.schema;

import com.demo.integrationplatform.dynamicrouterload.common.exception.JsonValidationException;
import com.demo.integrationplatform.dynamicrouterload.service.DynamicBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component("dynaVerify")
@Slf4j
public class DynamicValidator extends DynamicBase {

    @Autowired
    private DynamicValidatorService dynamicValidatorService;

    private final ObjectMapper jsonUtil = new ObjectMapper();

    public void process(String schemaName, Exchange exchange) throws Exception {
        // get Body & Headers
        Message msg = exchange.getMessage();
        if (null != msg) {
            Map<String, Object> allInOneMap = super.getHeaderBodyMap(msg);
            JsonSchema schema = null;
            Set<ValidationMessage> errors = null;
            if (MapUtils.isNotEmpty(allInOneMap)) {
                // Json should be got from DB
                schema = getSchemaByName(schemaName);
                String jsonStr = super.jsonUtil.writeValueAsString(allInOneMap);
                JsonNode jsonNode = super.jsonUtil.readTree(jsonStr);
                errors = doVerify(jsonNode, schema);
            }

            // if has ERROR
            if (null != errors && !errors.isEmpty()) {
                throw new JsonValidationException(exchange, schema, errors);
            }
        }
    }

    private JsonSchema getSchemaByName(String name) {
        VersionFlag schemaVerFlg = getSchemaVerFlg(SCHEMA_VAR_FLAG);
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(schemaVerFlg);
        String jsSchemaContent = dynamicValidatorService.selectValidatorCache(name);
        return schemaFactory.getSchema(jsSchemaContent);
    }

    private VersionFlag getSchemaVerFlg(String schemaVersion) {
        VersionFlag retFlg = null;
        if (StringUtils.isNotEmpty(schemaVersion)) {
            for (VersionFlag tmpFlg : VersionFlag.values()) {
                if (StringUtils.equalsIgnoreCase(tmpFlg.name(), schemaVersion)) {
                    retFlg = tmpFlg;
                    break;
                }
            }
        }

        return retFlg;
    }

    private Set<ValidationMessage> doVerify(JsonNode jsonNode, JsonSchema schema) {
        Set<ValidationMessage> errors = schema.validate(jsonNode);
        return errors;
    }
}