# Web Services and APIs 

## Summary
- [Web Services, Microservices and REST](./Web-Services-Microservices-and-REST.md)
- [GraphQL and Spring](./GraphQL-and-Spring.md)
- [Spring Cloud and Eureka](./Spring-Cloud-and-Edureka.md)
- [Security](./Security.md)

## To Keep
### H2
-   Enable the console, add a path for the console, and create a url for the datasource using H2.
```
spring.h2.console.enabled=true
spring.h2.console.path=/h2

spring.datasource.url=jdbc:h2:mem:dogdata
```

- @Entity: This marks a Java class as an entity, which means it will be persisted to the database. Typically, an entity maps to a database table and the table contains rows that represent that given entity.

- @RestController: This marks a class as a REST API. @RestController is a convenience annotation that combines @Controller and @ResponseBody.

- @GetMapping: This annotation handles HTTP GET requests and acts as a shortcut for @RequestMapping (method = RequestMethod.GET).