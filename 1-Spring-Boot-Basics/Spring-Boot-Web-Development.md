# Spring Boot Web Development
## The Spring Framework
**Spring is a framework for Inversion of Control**, which means that to use it, we have to package our code into individual component classes, telling Spring which components need each other to function. Spring takes the component classes we define and the dependencies we define between them and instantiates a system of matching Java objects with references to each other. This frees us from having to write so-called "glue code" to instantiate, connect, and manage components manually, and allows us to instead focus on writing so-called business logic, or code that concerns itself exclusively on the conceptual model of the application.

**Spring Matches dependency requests to resources by Java type, component name, or a configuration property.**

![what-spring-does.png](what-spring-does.png)

### A Bit More of Inversion of Control
- Define application Components and how to create them
- Define dependencies between components in a logical order
- It connects the initialized components according to their dependencies

### Key Terms
-   **Inversion of Control (IoC)**: A design pattern in which the developer creates independent application components and uses a framework to connect them, rather than writing the integration code themselves
-   **Business Logic**: Code that relates exclusively to an application's conceptual model and featureset. Contrast with utility code like database access, HTTP request parsing, etc.
-   **Persistent Components**: Java Objects that Spring maintains over time; they're persistent because they're created and maintained for as long as the application needs them to be.
-   **Dependency Injection**: A mechanism by which IoC may be implemented. Components are configured with dependencies on other components, which are injected at runtime. Injection is quite literal - a component's dependencies are usually expressed as annotated fields on the component class, and Spring will populate those fields with the dependencies at runtime.

## What is Spring?
When designing Spring applications, the most important principle to keep in mind is **separation of concerns**. What that really means is that for every problem your application has to solve has a home in a component class that is easy to find and flexible to use. By building your application out of small but focused components, you'll make the most of Spring's boilerplate-crushing power, and when it's time to add new features, you'll know exactly where to put them. On a larger team, this means greater coordination and less time spent trying to find and eliminate redundancies and conflicts.

But **in order to allow your components to communicate effectively with one another**, you'll **need a system of data types to share between them.** These are simple classes designed to store structured data, and little else. They're useful as conceptual representations of application data, like user profiles, or shipment invoice details, but made to be used and manipulated by the components of an application. A good rule of thumb to decide which is which is the new keyword test. A component should never be manually instantiated with the new keyword - components are managed by Spring, which means we need to rely on dependency injection to get a component reference. Data types, on the other hand, are no more special than Java's collections, and we can manually instantiate them with the new keyword whenever we'd like. Of course, we can use Spring (and its related libraries) to instantiate them for us as well.

### Key Terms:
-   **Separation of Concerns**: A code organization philosophy that emphasizes single-purpose components. In Java and Spring, this means keeping all methods related to a specific problem domain in the same class, for the sake of maintainability and reducing code reuse.
-   **Data Types**: Sometimes called POJOs (plain-old-java-objects), Data Types are classes in application designed purely to hold structured application data, like users, profiles, or anything else an application might manage. These objects are helpful for us to maintain a good conceptual model of an application, and are created and accessed frequently during execution.
-   **Components**: Components are persistent class instances managed by Spring in an application. They usually resemble libraries more than typical objects, with methods that must be called to perform specific actions. Components are only created when Spring is configured to create them, usually at server startup.

## Spring Boot IoC Configuration
Under the hood, Spring is just a Java application itself - and it responds to our configuration in a predictable way. When a **Spring application starts, it scans the code base for specially-marked class files and configuration options.** It uses that information to instantiate your application components as Java objects, and it stores them in a special data structure called the **application context**. This context is ultimately very similar to a `Map` or a python `dictionary`, and it can be queried at runtime to find specific components when needed. **This is a closed system, so components instantiated outside of Spring won't automatically be injected with dependencies like those instantiated by Spring**.

### Spring is a Closed System
- Beans in the application context are only aware of other beans in the same context.
- Spring components can only depend on other spring components.
- if the `new` keyword were use manually to instantiate a component class, Spring would have no ideia what was happening  and could not connect that instances with other beans in the application context.

![spring-application-context.png](spring-application-context.png)

The figure above shows an example of how Spring processes an IoC configuration. The general steps are:

-   A bean without dependencies is initialized first and placed within the application context.
-   A service is instantiated by Spring, and the first bean is retrieved from the app context to be injected as a dependency, after which Spring places the service in the application context.
-   Finally, another bean is initialized by Spring, which retrieves the previous two components to be injected as dependencies, after which the new bean is placed in the app context, and the application is fully initialized.

### Annotations to begin the Setup process
In a Spring Boot application, the basic setup of the Spring application context has already been done for us. The following annotations are the starting point of an application:

1.  **`@SpringBootApplication`**  - In a generated project starter, you're always given a main application class with the  `@SpringBootApplication`  annotation. This annotation is actually equivalent to three other annotations from Spring:  `@Configuration`,  `@EnableAutoConfiguration`, and  `@ComponentScan`. The  `@SpringBootApplication`  **configures Spring's component scanning and auto-configuration**.

2.  **`@Configuration`**  - It tells Spring that the annotated class includes component definitions for Spring to process. These take the form of  `@Bean`-annotated method whose return values are included as components in the application context. These methods can also take parameters, which act like the dependencies of the components returned by the methods.

3.  **`@EnableAutoConfiguration`**  - **It tells Spring** that it's okay to try **to match dependencies to components automatically**. Usually, that means dependencies are filled based on type, so in the example above, we have the  `compoundMessage`  method which depends on the  `basicMessage`  implicitly - the only String bean that Spring is aware of when constructing the  `compoundMessage`  is the  `basicMessage`, so it uses that.

#### Multiple Beans that Satisfy a Specific Dependency
When there are multiple beans that satisfy a specific dependency, Spring's auto-configuration needs some help to decide which to use. A common solution is to mark a bean with the `@Primary` annotation to indicate a universally-preferred bean for its type. Or, you can use pairs of `@Qualifier` annotations on beans and the dependencies that reference them to gain a finer level of control.

### Key Terms
-   **Configuration Files**: Project files that configure some part of Spring's operation. Some are embedded in Java classes, like we just discussed, and others are  `.properties`,  `.yaml`, and  `.xml`  files that we'll discuss later this lesson. Some of them configure the IoC context, like the ones we just discussed, and others configure more abstract pieces of Spring's system.
-   **Component Annotations**: Component annotations are annotations that identify application components for Spring to manage.  `@Bean`  and  `@Configuration`  are examples from the most recent videos, and in the next section we'll discuss  `@Component`  and  `@Service`  as well.
-   **Application Context**: Spring's application context is just a giant data structure that holds all application component instances. It can be queried to gain access to a specified component at runtime, and it's what Spring uses to resolve dependencies.
-   **Beans**: "Beans" are Spring's name for generic application components, and include any value Spring has stored in the application context. A bean is always either an object or primitive value.
-   **Closed System**: Spring's application context is a closed system, which means that it manages all of the components stored within. It is not possible to instantiate a component manually and still link it fully with Spring - it will never be aware of the components inside of Spring's application context, and vice versa.
-   **`@SpringBootApplication`**: An annotation put on the main application class of a Spring Boot project. It serves as an alias of three other annotations,  `@Configuration`,  `@EnableAutoConfiguration`, and  `@ComponentScan`
-   **`@Configuration`**: A class annotated with  `@Configuration`  is instantiated and managed by Spring as a component, but also as a bean factory. Any methods of the configuration class that are annotated with  `@Bean`  are used by Spring to create new beans to add to the application context.
-   **`@Bean`**: A method annotated with  `@Bean`  inside of a configuration class will be used by Spring to generate a bean of the method's return type. This means that the developer can manually configure beans to be included in the application context.
-   **`@EnableAutoConfiguration`**: A class annotated with  `@EnableAutoConfiguration`  tells Spring to try to automatically match beans to dependencies based primarily on type. This reduces the need for boilerplate code explicitly identifying individual beans as dependencies.
-   **`@Primary`**: This annotation distinguishes the annotated bean method as the default dependency of its type. This is used to resolve conflicts that arise from having multiple bean definitions of the same type when auto configuration is enabled.
-   **`@Qualifier`**: This annotation distinguishes the annotated bean method or dependency declaration as a qualified bean or dependency. Qualified beans are considered for unqualified dependencies, but only matching qualified beans are considered for qualified dependencies. You can read more about it  [here](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-autowired-annotation-qualifiers).

## Component and Services
If we want to declare custom classes as Spring Components, the best way to do it is to make use of `@ComponentScan`, an annotation that tells Spring to search your code base for classes annotated with `@Component`. These classes will automatically be instantiated as Spring beans, so there's no need to define an `@Bean`-annotated method if you already have `@Component` on you classes. There are other variants of `@Component` that identify specific roles for each component to play.

One important thing to keep in mind is that `@ComponentScan` only marks the _package_ of the class it's annotating for scanning - any classes outside of that package or its subpackages will be excluded by Spring. Here are the [official Spring docs for  `@ComponentScan`](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/ComponentScan.html) as well as the [official Spring docs explaining the how different  _stereotype_  annotations like  `@Component`,  `@Service`,  `@Repository`, and others, function](https://docs.spring.io/spring/docs/4.3.27.RELEASE/spring-framework-reference/htmlsingle/#beans-stereotype-annotations).

### Onion Architeture
![onion-architecture.png](onion-architecture.png)

The figure above shows the basic structure of Onion Architecture. External requests must first pass through a layer of controllers or request handlers whose only purpose is to handle these external requests. These controllers then use the next layer of the onion, the services, to process the actions or analysis triggered by the request. The services, in turn, use each other and the next layer, repositories and data access, to persist the results of the actions triggered by the request.

### Key Terms
-   **Onion Architecture**: An architectural pattern in which an application is separated into nested layers. In order for a request to be processed by the application, it must first travel through an outer layer of external interfaces and controllers, then through a middle layer of services and business logic, and finally through a persistence layer of data access objects. The separation of these layers emphasizes clean separation of concerns.
-   **Application Component**: In Spring, this is any @Component-annotated class that is instantiated by Spring and placed in Spring's application context. Architecturally speaking, this is a logical unit of an application - a single-purpose library or object that solves a particular problem an application faces.
-   **Service**: In Spring, this is any  `@Service`-annotated class, handled identically to an  `@Component`-annotated class. The difference between the two is semantics - a component is the most generic type of bean, and can be any kind of shared application structure. A service is specifically a collection of library methods that manage one aspect of an application's business logic. For example, a  `UserService`  would expose high-level actions related to the users of an application, and an  `AuthenticationService`  would expose actions for registering and authenticating a user. Services represent the middle layer of an onion architecture, and should contain the bulk of an application's business logic.
-   **Repository**: In Spring, an  `@Repository`-annotated class is treated identically to an  `@Component`-annotated one, but as with  `@Service`, the semantics are different. In an onion architecture, repositories are the inner layer - each repository should act like an interface to a specific set of stored or persistent data. For example, a  `UserRepository`  would expose an interface capable of create/read/update/delete and query operations on the  `users`  table of the database.

## Server-Wide Configuration
Spring Boot does a lot to simplify the setup of a new Spring application, but sometimes, the default configuration must be changed in order to address some project requirement. That's what the  `application.properties`  file is for! It can found in the  `src/main/resources`  folder of a generated Spring Boot project, and it allows to configure anything from the server's hostname and port to the size and colors of the Spring logo that appears in the console when starting an application.

[Doc - all of the available config options can be found here.](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html)

### Key Terms
-   **Properties File**: A file with the  `.properties`  extension that consists of plain-text  `key=value`  pairs separated by new lines. This format is often used by Java libraries and frameworks because it provides a simple way to specify named constants in a plain text file.

### Example -  `application.properties` file
```properties
# config port and host
server.port=8080
server.address=localhost

# log level
logging.level.com.udacity.jdnd.course1exercises=DEBUG

# config ssl
server.ssl.enabled=true
server.ssl.protocol=TLS

# enable template caching for Thymeleaf'
spring.thymeleaf.cache=true

# enable remote restart through devtools
spring.devtools.remote.restart.enabled=true

# error URL path
server.error.path=/error
# enable default error page
server.error.whitelabel.enabled=true


# request busy queue length
server.tomcat.accept-count=100


# set console banner character set
spring.banner.charset=UTF-8
# set console banner height
spring.banner.image.height=20


# config database url and credentials
spring.datasource.url=jdbc:postgres://localhost:5432
spring.datasource.username=admin
spring.datasource.password=password
```


## When You have to Use XML Configuration

Refer to the video -> XML-Configuration-vs-Java-Configuration.mp4

### XML Configuration vs Java Configuration
```
<beans>
  <bean
      id="indexService"
      class="com.udacity.example.IndexService" />
  <bean
      id="indexApp"
      class="com.udacity.example.IndexApp">
    <constructor-arg ref="indexService" />
  </bean>
</beans>
```
### Java Configuration
```java
// IndexService.java
@Service
public class IndexService {
  // ...
}

// IndexApp.java
@Component
public class IndexApp {
  private IndexService indexService;

  public IndexApp (IndexService indexService) {
    this.indexService = indexService;
  }
}
```

-   [Here are the official Spring docs for annotation-based configuration, which feature a discussion of XML vs. annotations and many examples of how the two relate to each other](https://docs.spring.io/spring/docs/4.3.27.RELEASE/spring-framework-reference/htmlsingle/#beans-annotation-config).

## Lesson 3 - Glossary

-   **Inversion of Control (IoC)**: A design pattern in which the developer creates independent application components and uses a framework to connect them, rather than writing the integration code themselves
-   **Business Logic**: Code that relates exclusively to an application's conceptual model and featureset. Contrast with utility code like database access, HTTP request parsing, etc.
-   **Persistent Components**: Java Objects that Spring maintains over time; they're persistent because they're created and maintained for as long as the application needs them to be.
-   **Dependency Injection**: A mechanism by which IoC may be implemented. Components are configured with dependencies on other components, which are injected at runtime. Injection is quite literal - a component's dependencies are usually expressed as annotated fields on the component class, and Spring will populate those fields with the dependencies at runtime.
-   **Separation of Concerns**: A code organization philosophy that emphasizes single-purpose components. In Java and Spring, this means keeping all methods related to a specific problem domain in the same class, for the sake of maintainability and reducing code reuse.
-   **Data Types**: Sometimes called POJOs (plain-old-java-objects), Data Types are classes in application designed purely to hold structured application data, like users, profiles, or anything else an application might manage. These objects are helpful for us to maintain a good conceptual model of an application, and are created and accessed frequently during execution.
-   **Components**: Components are persistent class instances managed by Spring in an application. They usually resemble libraries more than typical objects, with methods that must be called to perform specific actions. Components are only created when Spring is configured to create them, usually at server startup.
-   **Configuration Files**: Project files that configure some part of Spring's operation. Some are embedded in Java classes, like we just discussed, and others are  `.properties`,  `.yaml`, and  `.xml`  files that we'll discuss later this lesson. Some of them configure the IoC context, like the ones we just discussed, and others configure more abstract pieces of Spring's system.
-   **Component Annotations**: Component annotations are annotations that identify application components for Spring to manage.  `@Bean`  and  `@Configuration`  are examples from the most recent videos, and in the next section we'll discuss  `@Component`  and  `@Service`  as well.
-   **Application Context**: Spring's application context is just a giant data structure that holds all application component instances. It can be queried to gain access to a specified component at runtime, and it's what Spring uses to resolve dependencies.
-   **Beans**: "Beans" are Spring's name for generic application components, and include any value Spring has stored in the application context. A bean is always either an object or primitive value.
-   **Closed System**: Spring's application context is a closed system, which means that it manages all of the components stored within. It is not possible to instantiate a component manually and still link it fully with Spring - it will never be aware of the components inside of Spring's application context, and vice versa.
-   **`@SpringBootApplication`**: An annotation put on the main application class of a Spring Boot project. It serves as an alias of three other annotations,  `@Configuration`,  `@EnableAutoConfiguration`, and  `@ComponentScan`
-   **`@Configuration`**: A class annotated with  `@Configuration`  is instantiated and managed by Spring as a component, but also as a bean factory. Any methods of the configuration class that are annotated with  `@Bean`  are used by Spring to create new beans to add to the application context.
-   **`@Bean`**: A method annotated with  `@Bean`  inside of a configuration class will be used by Spring to generate a bean of the method's return type. This means that the developer can manually configure beans to be included in the application context.
-   **`@EnableAutoConfiguration`**: A class annotated with  `@EnableAutoConfiguration`  tells Spring to try to automatically match beans to dependencies based primarily on type. This reduces the need for boilerplate code explicitly identifying individual beans as dependencies.
-   **`@Primary`**: This annotation distinguishes the annotated bean method as the default dependency of its type. This is used to resolve conflicts that arise from having multiple bean definitions of the same type when auto configuration is enabled.
-   **`@Qualifier`**: This annotation distinguishes the annotated bean method or dependency declaration as a qualified bean or dependency. Qualified beans are considered for unqualified dependencies, but only matching qualified beans are considered for qualified dependencies. You can read more about it  [here](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-autowired-annotation-qualifiers).
-   **Onion Architecture**: An architectural pattern in which an application is separated into nested layers. In order for a request to be processed by the application, it must first travel through an outer layer of external interfaces and controllers, then through a middle layer of services and business logic, and finally through a persistence layer of data access objects. The separation of these layers emphasizes clean separation of concerns.
-   **Application Component**: In Spring, this is any @Component-annotated class that is instantiated by Spring and placed in Spring's application context. Architecturally speaking, this is a logical unit of an application - a single-purpose library or object that solves a particular problem an application faces.
-   **Service**: In Spring, this is any  `@Service`-annotated class, handled identically to an  `@Component`-annotated class. The difference between the two is semantics - a component is the most generic type of bean, and can be any kind of shared application structure. A service is specifically a collection of library methods that manage one aspect of an application's business logic. For example, a  `UserService`  would expose high-level actions related to the users of an application, and an  `AuthenticationService`  would expose actions for registering and authenticating a user. Services represent the middle layer of an onion architecture, and should contain the bulk of an application's business logic.
-   **Repository**: In Spring, an  `@Repository`-annotated class is treated identically to an  `@Component`-annotated one, but as with  `@Service`, the semantics are different. In an onion architecture, repositories are the inner layer - each repository should act like an interface to a specific set of stored or persistent data. For example, a  `UserRepository`  would expose an interface capable of create/read/update/delete and query operations on the  `users`  table of the database.
-   **Properties File**: A file with the  `.properties`  extension that consists of plain-text  `key=value`  pairs separated by new lines. This format is often used by Java libraries and frameworks because it provides a simple way to specify named constants in a plain text file.
-   **Legacy**: In a programming context,  _legacy_  usually refers to older code that still functions or is expected to function, but is on the verge of being made obsolete by newer technologies. A legacy application is one that is no longer being actively built upon, and is instead in maintenance mode.
-   **XML**: e**X**tensible  **M**arkup  **L**anguage. This is a flexible data format that allows for extension, as the name suggests. Many applications and libraries use XML as a way to store structured application data out of memory, and it's also a popular data interchange format on the web.

## Reference
### Spring Boot IoC Configuration
-   [Official Spring IoC Documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans)
-   [Official Spring Annotation-Based Configuration Documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-annotation-config)