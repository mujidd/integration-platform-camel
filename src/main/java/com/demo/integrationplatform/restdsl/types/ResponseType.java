package com.demo.integrationplatform.restdsl.types;

public class ResponseType {

    private String message;

    public ResponseType() {
    }

    public ResponseType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
