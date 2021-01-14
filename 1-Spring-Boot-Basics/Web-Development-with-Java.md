# Web Development with Java
## Programming for the Web in Java
![Java-Application-Server-Connects-Applications-to-the-Web](Java-Application-Server-Connects-Applications-to-the-Web.png)

**A web server's primary role is listening for HTTP requests and handling them with application logic**, sending an HTTP response to the client that indicates the result of the operation. Simple HTTP servers host directories of HTML files directly, sending files in response to requests for specific URLs. This is enough for static websites with no dynamic server operations, but modern web apps support users accounts, complex interactions, and persistent data. **Java application servers** make these features more accessible by **hosting many individual applications, managing them over a common interface, the _servlet_**. This allows developers to focus on application logic and features, with HTTP request handling and routing handled by the server.

> **Q:** Choose the correct statement about Java Application Servers and their place in web development.
> **A:** They host many specialized applications in parallel
> 
> **Comment:** **_Application servers_ do the hard work of parsing and routing requests**, passing them to individual applications through a common interface, the servlet. Those specialized applications can perform complex logic before sending a response back through the servlet for the server to pass on to the client.

### Key Terms

-   **HTTP Request/Response:**  HTTP, or  **H**yper**T**ext**T**ransfer**P**rotocol, is a binary data format for communication between programs over the web. It can be broken down into two basic message types: requests and responses. Clients send requests for resources to servers, which respond with the requested data.  [Read more about the protocol here.](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages)

-   **HTTP  `GET`  and  `POST`:**  Every HTTP request has an important header that determines its  _method_.  `GET`  and  `POST`  are two of the most common;  `GET`  indicates a request for data from the server, and  `POST`  represents a request to "post" new data to the server - this usually represents some action on server data like submitting search terms, posting an update, or adding new data to the server.

## Web Development with Java
Fundamentally, a web application needs to be able to process HTTP requests and respond to them. It also needs to manage concurrent requests, managing many client connections in parallel.

When developing in Java, the solution is to use an **application server** as specified by Java EE.

But which **application server** to use?
- GlassFish
- Apache Tomcat
- Netty
- WildFly

### Abstraction: Spring Boot
Spring boot is a platform that includes an embedded application server that can be reconfigured to use a different underlying server at any time.

It includes apache tomcat by default. The application development can be started and the decision of which application server use can be done later if needed.

![abstraction-spring-boot](abstraction-spring-boot.png)

## Java Application Server

### Basic Mechanics of Server Connections 
A cliente connects to a server host and port opening a socket that allows bit of data to be send and receive using some kind of protocol such as HTTP.

### What is and How Java Application Server?
A Java Application Server is a _pluggable architecture_ that can host many deployed applications at once. It does not actually provide  any application logic itself. Instead, it provides utilities like multi-threading, request filtering, and resource sharing to each application. Those applications must expose endpoints that handle the requests routed to them by the server.
> Q: Choose the correct benefits of a Java application server.
> A: 
> -   It automatically handles multiple client connections simultaneously.  
> -   It can be configured to forward requests with custom logic
> -   It can share heavyweight or universal components with each of its application

> **Comment**: Application servers provide utilities and resources to the applications they host, but they don't perform any of what we call _business logic_

![structure-java-application-server](structure-java-application-server.png)

### Thins to Remember
- apps are managed and run by java application servers
- they can access resources provided by the server
- there may be many other apps on the same server

### Key Terms
-   **HTTP**: Hypertext Transfer Protocol. A binary protocol that originally defined the mechanics of requesting and sending HTML over the internet.
-   **Web Server**: A program that listens for and responds to HTTP requests over the internet
-   **Application Server**: **A program that hosts other applications, forwarding incoming requests to the appropriate application according to a filter**. Provides shared access to resources and multi-threading.
-   **Pluggable Architecture**: A pluggable architecture refers to any piece of software that allows parts of it to be added, replaced, and removed. Usually, this is achieved through a common interface for every "pluggable" component. Sometimes the architecture can even replace components at runtime, as is the case with Servlets in an Application Server.
-   **Threads/Threading**: These terms come from concurrent programming - a thread is essentially one track of computation, and multi-threading is running multiple threads in parallel. This gets a little complicated because your CPU has a limited number of physical cores that can process instructions in parallel, while the number of threads you can have can be many more than your computer has cores.
- **Streaming connections**: A protocol can use a format of streaming connection where data can be rececive and sent continuously

## Java Servlets
The **core of a Java Web Application** is the servlets. **Servelets are the interface** between an application and the Java Application Server it runs on. Each servlet in an application represents a configurable endpoint for client connections.

The `Servlet` class is the main connection between the apps you develop and the application server they run on. By extending `Servlet`, you can create endpoints that handle incoming requests in a way specific to your application needs, and you can map specific request URLs to specialized servlets by adding entries to a `web.xml` file. The app server uses this configuration to instantiate and manage your servlets. It interacts with each through three standard methods, `init`, `service`, and `destroy`:

-   `service`  is where requests are handled, and the server will call this method when a request is routed to the servlet it's called on.
-   `init`  is where initialization of the servlet is handled, and the server will call this method directly after instantiating the servlet.
-   `destroy`  is where servlet resource cleanup is handled, and is called directly before the server terminates the servlet instance.

![lifecycle-of-servlet-application](lifecycle-of-servlet-application.png)

> Q: What's the relationship between the Application Server and a Servlet?
> A:  Application Servers receive HTTP requests, parse the information, and decide which Servlet should receive a request object.

### Java Application Files
When you compile a Java program and  _package_  it to be run, the Java compiler creates what is called a  **J**ava  **AR**chive, or JAR file. This file contains a compressed file hierarchy, with folders that represent Java packages that contain Java  `.class`  files, which are the compiled versions of  `.java`  source code files. It can also contain arbitrary resource files, either at the root level or deeply nested in the package hierarchy. These files often contain metadata related to the app or library contained in the JAR file, which can be read by any program that interacts with the JAR.

When you want to deploy an app to an app server, you have to package it as a  **W**eb application  **AR**chive, or WAR file. A WAR file is almost identical to a JAR file, but includes configuration files specific to web applications. When we copy a WAR file into the deployment directory of an app server, the server unpackages it, looks for a  `web.xml`  file, and uses that file to find the classes and resources required by the application. This uses advanced Java features like reflection and class loading to programmatically load Java class definitions and instantiate them. It allows to dynamically load, start, stop, and replace any number of applications in a web server at any time.

### Thins to Remember
- a servlet implementation must have a service() method which encapsulates the servlet's client-handling logic.
- A servlet can be registred with the application server using web.xml configuration file, which maps specifc requests paths to specific servlets

### Key Terms
-   **Endpoints**: An  _endpoint_  is the address at which a client can reach a specific part of a server's functionality. Usually, this is a URL path, the  `/words/and/slashes`  that follow the domain of a URL, like  `.com`  or  `.org`.
-   **`Servlet`**: A class defined as a part of the Java: Enterprise Edition specification. Provides an implementable interface for web server request processing, defining a  `service`  method that the server invokes on an instantiated servlet to handle a  `ServletRequest`  and  `ServletResponse`  object for an incoming request. The servlet also defines lifecycle methods for the server to invoke when initializing or destroying a servlet.
-   **JAR**: A Java Archive file, which stores compiled  `.class`  files in a folder hierarchy that matches the code's package structure. Includes an optional manifest file.
-   **WAR**: A variation on the JAR for web applications, which optionally includes web resources like HTML files and configuration files like  `web.xml`  for servlet registration/mapping.

## Spring Applications
Servlets and Application Servers together form a powerful platform, and on top of this platform lies Spring.

Unlike a library from which we would pick and choose specific APIs to invoke from our application, we use the application frame work to instead invoke our application's API's in some pre-defined way. This pattern is colled IoC.

We can define individual application components as classes that the Spring IoC instantiate and manages.

When Spring is used in a web application, the Spring container is piggybacking on the servelet instance. It means that the overall life cycle of our spring application compoenents is bounded by the lifecycle of the servlets where the Spring is running on. 

![springs-place-in-the-app-server.png](springs-place-in-the-app-server.png)

### Spring Application Framework - A Little More of Details
Spring is an  _application framework_, which means that instead of choosing when to invoke it from your application, you choose when it invokes your application code. This pattern of software development is called **Inversion of Control (IoC)**, and it's powerful because it allows developers to develop specialized application components and use Spring to connect them with each other using **dependency injection**. This is good for clean, separated code and for code reuse. This is evident when looking at the vast number of Spring modules and Spring-integrated third-party tools that are available.

-   **Spring MVC**, a generic web controller library for Spring that supports a wide variety of utilities to simplify the handling of HTTP requests
-   **Thymeleaf**, a third party template engine that can integrate with Spring MVC to simplify the generation of web pages as responses to HTTP requests
-   **Spring Security**, a generic authentication library for Spring that can integrate with many different credential sources and authentication protocols to automatically manage request authentication and security contexts
-   **MyBatis**, a third-party database access library that provides simple SQL/Java mapping tools that can be defined in Spring components

> Q1 : What’s the relationship between Spring and a Java Application Server?
> A: Spring dispatches Servlet requests from the Application Server to specific Java code to handle the requests.
> **Comment**: Spring makes it much simpler for our Java applications to communicate with the Application Server by serving as a go-between for Servlet requests.

> Q2: Inversion of Control is one of the main features of Spring. It allows Spring to manage instances of dependencies and provide them when needed.
We could say that Spring’s ability to inject dependencies is just like the Application Server’s ability to provide what?
> A: Servlets
> **Comment**: The Application Server manages as many Servlets as needed to receive HTTP requests. It can instantiate, call, and destroy Servlet instances. Spring does something similar with dependencies you define.

### The End of Boilerplate: Spring Boot
So Spring adds a lot of features, but it still sounds like a lot of configuration. We still have to deploy it to an application server, right? And we still have to create a servlet for Spring to live in. It also sounds like getting all of these modules and utilities to work together might take some work.

In the past, Spring did require a lot of configuration, but over time, the development world has moved towards a convention-over-configuration approach. Spring Boot is a project that provides an a-la-cart Spring experience, complete with a web page for generating and downloading starter projects based on the application needs. Most Spring Boot applications today also contain an embedded application server with a default, pre-configured servlet definition. All you have to do to run your Spring-enabled code as a server is to run a main method.

With the rise of containerized architectures like Docker, this style of application development has become as popular as the pluggable application server. However, if you do want to deploy your Spring Boot application to a traditional application server, there are built-in tools that allow you to package the application as a standard WAR file.

### Key Terms
-   **IoC**: Inversion of Control, which is the practice of designing libraries as  _application runners_. This allows developers to focus on application-specific logic and rely on IoC containers to connect application components with one another, eliminating a lot of boilerplate and encouraging a clean separation of development concerns.

## Spring Starter Packs
Spring is a collection of open-source libraries that solve common web development problems. But how do those libraries can be added to the project? It can be added by using Maven/Gradle, a dependency management tool that lets us define dependencies on open-source libraries based on their names and version numbers. 

### Maven
The starters are defined in a file in the projects called pom.xml, which Maven reads and uses to download the required libraries. Also, The project have inherited dependencies from some base project, which is a feature that Spring Boot uses to make setting up a new Spring project easy .

### Example of  Spring Starters
-   [Spring Dev Tools](https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-devtools.html): utilities including hot reloading changed code into a running application
-   [Spring MVC](https://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/mvc.html): web layer utilities that make developing server-side web apps easy
-   [Spring Data](https://spring.io/projects/spring-data): a common interface for many different types of database access

### Key Terms
-   **Maven**: A dependency management system and project build tool for Java. Provides a standardized way to define dependencies between projects and include them in the project build path.
-   **POM**: The  **P**roject  **O**bject  **M**odel that Maven uses to represent the dependency and build configuration of a project. Usually, this refers to the  `pom.xml`  configuration file found in a Maven project.
-   **Dependency Management System**: Any tool that automates the downloading and linking of external packages to a software development project. Most languages these days either provide one officially or have a community-accepted standard.

## Edge Case - When You Can't Use Spring Boot
Sometimes, it's not an option to use Spring Boot in a project. Usually, this is because some form of IoC has already been implemented, or it would take a lot of time to rewrite the code to use Spring Boot. Sometimes, though, it's worth adding Spring or Spring Boot to a project by refactoring the code and adding the Spring Servlet manually.

> Consider the following scenarios and decide if you can use Spring Boot in each of them. Mark each scenario where Spring Boot is a viable choice.
> -   [ ] project that already uses Spring   
> -   [ ] project that uses Java EE Enterprise Java Beans and JNDI 
> -   [X] project that uses Oracle WebLogic as an Application Server 
> -   [X] project that uses plain Servlets to handle requests
> Comment: There are situations where Spring Boot is not a good choice - usually, it's because the application already has some kind of IoC implementation

## References
### Java Servlets
-   Official documentation of the  [`Servlet`  API](https://javaee.github.io/tutorial/servlets.html#BNAFD)
-   Official documentation for the  [Java JAR API](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/jar/package-summary.html)
-   Official documentation for  [packaging WAR files](https://javaee.github.io/tutorial/packaging003.html#BCGHAHGD)