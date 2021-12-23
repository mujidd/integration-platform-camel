package com.demo.integrationplatform.restdsl.beans;

import com.demo.integrationplatform.restdsl.types.PostRequestType;
import com.demo.integrationplatform.restdsl.types.ResponseType;
import org.springframework.stereotype.Component;

@Component
public class PostBean {

    public ResponseType response(PostRequestType input) {
        // We create a new object of the ResponseType
        // Camel will be able to serialise this automatically to JSON
        return new ResponseType("Thanks for your post, " + input.getName() + "!");
    }
}
