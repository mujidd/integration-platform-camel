# integration-platform-camel


This example illustrates how to use https://projects.spring.io/spring-boot/[Spring Boot] with http://camel.apache.org[Camel]. It provides a simple REST service that is created with http://camel.apache.org/rest-dsl.html[Camel REST DSL] and documented with http://swagger.io[Swagger].

The project uses the `camel-spring-boot-starter` dependency, a Spring Boot starter dependency for Camel that simplifies the Maven configuration.

The project also uses `camel-servlet` component as the HTTP transport component for Camel REST DSL.


$ curl http://localhost:8080/api/users

The Swagger documentation is located at: `\http://localhost:8080/api/api-doc` and can be retrieved with the following command:

$ curl http://localhost:8080/api/api-doc

hawtio url：
http://localhost:8080/actuator/hawtio/
