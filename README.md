# integration-platform-camel


This example illustrates how to use https://projects.spring.io/spring-boot/[Spring Boot] with http://camel.apache.org[Camel]. It provides a simple REST service that is created with http://camel.apache.org/rest-dsl.html[Camel REST DSL] and documented with http://swagger.io[Swagger].

The project uses the `camel-spring-boot-starter` dependency, a Spring Boot starter dependency for Camel that simplifies the Maven configuration.

The project also uses `camel-servlet` component as the HTTP transport component for Camel REST DSL.

## Rest DSL

To test the REST service's GET operation - see how it returns a JSON object:

    $ curl http://localhost:8080/api/rest/
    {"message":"Hello, world!"}

And to test the REST service's POST operation - see how it returns a JSON object:

    $ curl --request POST \
        --data "{ \"name\": \"Jeff Mills\" }" \
        --header "Content-Type: application/json" \
        http://localhost:8080/api/rest/
    {"message":"Thanks for your post, Jeff Mills!"}

## Rest DSL & Swagger

The Swagger documentation is located at: `\http://localhost:8080/api/api-doc` and can be retrieved with the following command:

    $ curl http://localhost:8080/api/api-doc

    $ curl http://localhost:8080/api/users
## hawtio
    http://localhost:8080/actuator/hawtio/
