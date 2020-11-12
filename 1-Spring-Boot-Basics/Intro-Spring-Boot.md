# Intro Spring Boot
## Why Learn Web Development
Learning web development is valuable because it's how the digital apps and services that we use every day are built. Being able to visualize the architecture of a web application will make you a better developer and give you tools to use when turning an app idea into reality.

Learning Java is valuable because it's a widely-used industry mainstay, and it has a massive community of developers and open-source projects, like Spring Boot. Learning Spring Boot matters because it's a core Java library that supports and defines language-wide best practices. Mastery of Spring will make you an excellent Java developer and expose you to the Java-standard solutions to a wide range of common problems. Learning to recognize those common problems will make you a better developer. Wherever you go, whatever language you use, the same problems will arise, and you'll know what kind of solutions to look for.

## Spring
Spring is a framework that encompasses many useful Java libraries for web development. Spring includes numerous essential web development "_components_", such as database access, security, cloud deployment, web services, and many more.

> The Spring Framework is divided into modules. Applications can choose which modules they need. At the heart are the modules of the core container, including a configuration model and a dependency injection mechanism. Beyond that, the Spring Framework provides foundational support for different application architectures, including messaging, transactional data and persistence, and web.

## Spring Boot
Spring Boot is a part of the Spring framework, which helps in rapid application development. Spring Boot helps to develop stand-alone and enterprise web applications.  _The advantage of using Spring Boot is that developers can get started with Spring's core libraries with a minimum configuration setup._  Therefore, Spring Boot helps to speed up application development.

> Spring Boot, applications are created in a devops and cloud-friendly way, with the Servlet container embedded and trivial to change.

> Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)

## Spring versus Spring Boot
Both Spring and Spring Boot frameworks help to build microservices-based web applications. Internally, the Spring Boot is built on top of the Spring framework. In general, the  [Spring (core module)](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/overview.html#overview-history)  takes care of the actions, such as:

1.  Inversion of Control (IoC)
2.  Dependency injection
3.  Common annotations
4.  Implements and provides the foundation for other frameworks, such as Thymeleaf, MyBatis, Spring MVC, and many others.

Whereas,  [Spring Boot](https://spring.io/projects/spring-boot)  brings all the functionality of Spring and some additional advantages, such as:

1.  It can automatically configure Spring and third-party libraries
2.  It provides necessary dependencies to ease the build configuration

This way, Spring Boot helps to speed up application development. It is a matter of software requirements to choose between Spring and Spring Boot frameworks. Either of the frameworks can be used to build microservices-based web applications. However, Spring Boot helps to get things done faster.

## When to Use Spring Boot
If your project aims to connect simultaneous users on different clients, you probably need to build a server, which Spring Boot can help with. If you're making a website with dynamic data, Spring Boot provides  **Thymelea**f as an HTML template engine. If you store and manage data in a database, Spring Boot supports a plethora of Java libraries that provide database access, including  **MyBatis**, which we cover in this course. As long as you need a server, Spring Boot is probably the right choice.

If you need to test your application by automating a web browser,  **Selenium**  is the industry standard. There are libraries for many languages to use Selenium, and Java is one of them. Selenium also has integrations with Spring Boot that make testing server behavior a breeze.

## History Web Development
Networked computing has existed for nearly sixty years. First, specific programs were designed to speak to each other, but over the years several standards for different data protocols were established. In this era, the server/client dichotomy reigned supreme, with specialized server and client programs developed for individual network tasks like file sharing and email exchange. This changed with the development of the world wide web, HTTP, and HTML. As web sites became more complex, the web servers took on more and more roles as the mediator between the user's browser and other relevant services. This amalgamation of web server responsibilities coincided with the release and popularity of Java, which was soon accompanied by official web servlet and application container specifications which solved common problems when constructing complex, feature-rich web servers.

With the rise of the web, software developers began to meet and share ideas in open source communities online. These groups developed useful tools and utilities, including Spring, which aimed to build on Java's rich server architecture and provide a boilerplate-free enterprise development experience. It has grown into an industry standard, and an umbrella project that supports a vast array of powerful utilities and frameworks.