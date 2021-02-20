### Add the Maven dependency
Add the  [Java JWT Maven dependency](https://mvnrepository.com/artifact/com.auth0/java-jwt)  to your  `pom.xml`
```
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.11.0</version>
</dependency>
```

> Note: The version may vary in the demo video above, and the current version available.

Refer to the list of available algorithms, and the usage (create, verify, decode a token) of the above library in the  [README](https://github.com/auth0/java-jwt/blob/master/README.md)  here.

### Create the security package
As demonstrated in the video above, create the  `src/main/java/com/example/demo/security`  folder and the Java file to place in that folder are available for view/download at the bottom of the current page.

> **Note**  - If you download the Java files attached below, then the system will automatically change the name (letter-case) of the file.

The details of the Java files are explained below:

### 1. JWTAuthenticationFilter.java
This custom class is responsible for the authentication process. This class extends the  `UsernamePasswordAuthenticationFilter`  class, which is available under both  [**spring-security-web**](https://mvnrepository.com/artifact/org.springframework.security/spring-security-web)  and  [**spring-boot-starter-web**](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)  dependency. The Base class parses the user credentials (username and a password). You can have a look at all the available methods of the Base class  [here](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.html).

We have overridden the following two methods:

1.  `attemptAuthentication()`  - It performs actual authentication by parsing (also called filtering) the user credentials.    
    ```
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                           HttpServletResponse res) throws AuthenticationException {
    try {
       User credentials = new ObjectMapper()
               .readValue(req.getInputStream(), User.class);
    
       return authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       credentials.getUsername(),
                       credentials.getPassword(),
                       new ArrayList<>()));
    } catch (IOException e) {
       throw new RuntimeException(e);
    }
    }    
    ```
    
2.  `successfulAuthentication()`  - This method is originally present in the parent of the Base class. After overriding, this method will be called after a user logs in successfully. Below, it is generating a String token (JWT) for this user.    
    ```
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                       HttpServletResponse res,
                                       FilterChain chain,
                                       Authentication auth) throws IOException, ServletException {
    
    String token = JWT.create()
           .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
           .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
           .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
    res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }    
    ```    

### 2. SecurityConstants.java
This class contains the literal constants that are used in the  _JWTAuthenticationFilter_  class.

### 3. JWTAuthenticationVerficationFilter.java
This class is responsible for the  **authorization**  process. This class extends the  `BasicAuthenticationFilter`  class. It overrides on method, and defines another custom method.

1.  Custom method -  `getAuthentication()`  - It validates the token read from the Authorization header.    
    ```
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
      String token = req.getHeader(SecurityConstants.HEADER_STRING);
      if (token != null) {
          String user = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes())).build()
                  .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                  .getSubject();
          if (user != null) {
              return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
          }
          return null;
      }
      return null;
    }    
    ```
        
2.  Overridden method -  `doFilterInternal()`- This method is used when we have multiple roles, and a policy for RBAC.    
    ```
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
          throws IOException, ServletException {
      String header = req.getHeader(SecurityConstants.HEADER_STRING);
    
      if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
          chain.doFilter(req, res);
          return;
      }
    
      UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
    
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(req, res);
    }    
    ```    

### 4. WebSecurityConfiguration.java
After defining the authentication and authorization modules, we need to configure them on the Spring Security filter chain. The  `WebSecurity`  class is a custom implementation of the default web security configuration provided by Spring Security. In this class, we have overridden two overloaded methods:

1.  `configure(HttpSecurity)`  - Defines public resources. Below, we have set the  `SIGN_UP_URL`  endpoint as public. The  `http.cors()`  is used to make the Spring Security support the CORS (Cross-Origin Resource Sharing) and CSRF (Cross-Site Request Forgery). Read more  [here](https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/cors.html).    
    ```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.cors().and().csrf().disable().authorizeRequests()
              .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
              .anyRequest().authenticated()
              .and()
              .addFilter(new JWTAuthenticationFilter(authenticationManager()))
              .addFilter(new JWTAuthenticationVerficationFilter(authenticationManager()))
              .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }    
    ```
    
2.  `configure(AuthenticationManagerBuilder)`  - It declares the BCryptPasswordEncoder as the encoding technique, and loads user-specific data.    
    ```
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.parentAuthenticationManager(authenticationManagerBean())
          .userDetailsService(userDetailsService)
          .passwordEncoder(bCryptPasswordEncoder);
    }    
    ```    

### 5. UserDetailsServiceImpl.java
It implements the  `UserDetailsService`  interface, and defines only one method that retrieves the User obejct from the database:
```
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
```

### Recommended Read
-   StackOverflow discussion thread on  [How Spring Security Filter Chain works](https://stackoverflow.com/questions/41480102/how-spring-security-filter-chain-works)
-   [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)