# Spring MVC
## Implementing `WebMvcConfigurer`
### `ViewControllerRegistry`
The `ViewControllerRegistry`assists with the registration of simple automated controllers pre-configured with status code and/or a view.

```java
registry.addViewController
registry.addStatusController(urlPath, HttpStatus statusCode)
registry.addRedirectViewController(urlPath,redirectUrl)
```

### `ResourceHandlerRegistry`
```java
// registry is of the type ResourceHandlerRegistry
registry.addResourceHandler("/**/*.css", "/**/*.js")
        .addResourceLocations("classpath:/static/");
```