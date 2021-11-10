package com.demo.integrationplatform.restdsl;

import java.util.Collections;
import java.util.Map;

import com.demo.integrationplatform.restdsl.types.ResponseType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Used for simulating a rest service which we can run locally inside Spring Boot
 */
@RestController
public class PetController {

    private static final String[] PETS = new String[]{"Snoopy", "Fido", "Tony the Tiger"};

    @GetMapping(value = "/pets/{id}")
    public ResponseType petById(@PathVariable("id") Integer id) {
        if (id != null && id > 0 && id <= PETS.length) {
            int index = id - 1;
            String pet = PETS[index];
            return new ResponseType(Collections.singletonMap("name", pet).toString());
        } else {
            return new ResponseType();
        }
    }

}
