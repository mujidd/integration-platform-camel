package com.demo.integrationplatform.dynamicrouterload.common.exception;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;

import java.util.Set;

public class JsonValidationException extends ValidationException {

    private final JsonSchema schema;
    private final Set<ValidationMessage> errors;

    public JsonValidationException(Exchange exchange, JsonSchema schema, Set<ValidationMessage> errors) {
        super(exchange, "JSON validation error with " + errors.size() + " errors");
        this.schema = schema;
        this.errors = errors;
    }

    public JsonValidationException(Exchange exchange, JsonSchema schema, Exception e) {
        super(e.getMessage(), exchange, e);
        this.schema = schema;
        this.errors = null;
    }

    public JsonSchema getSchema() {
        return schema;
    }

    public Set<ValidationMessage> getErrors() {
        return errors;
    }

    public int getNumberOfErrors() {
        return errors.size();
    }
}
