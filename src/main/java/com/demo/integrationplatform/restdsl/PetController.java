package com.demo.integrationplatform.restdsl;

import com.demo.integrationplatform.restdsl.types.ResponseType;
import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.spi.Resource;
import org.apache.camel.support.ResourceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Used for simulating a rest service which we can run locally inside Spring Boot
 */
@RestController
public class PetController {

    private static final String[] PETS = new String[]{"Snoopy", "Fido", "Tony the Tiger"};

    @Autowired
    CamelContext camelContext;

    @PostMapping(value = "/pets/{id}")
    public ResponseType petById(@PathVariable("id") Integer id) {
        if (id != null && id > 0 && id <= PETS.length) {
            int index = id - 1;
            String pet = PETS[index];
            return new ResponseType(Collections.singletonMap("name", pet).toString());
        } else {
            return new ResponseType();
        }
    }

    @PostMapping(value = "/router")
    public void petById(@RequestBody String routerDifinition) throws Exception {
        ExtendedCamelContext ecc = camelContext.adapt(ExtendedCamelContext.class);
        Resource resource = ResourceHelper.fromString("resource.xml", routerDifinition);
        ecc.getRoutesLoader().loadRoutes(resource);
    }

}
