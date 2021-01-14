# Data Persistence and Security
-   **ORM is the Norm:** ORM introduction, or **object-relational-mapping**, a software pattern that leverages the similarities between Java classes and SQL tables to eliminate boilerplate in data access code.
-   **MyBatis Mappers:**  MyBatis introduction , a dead-simple ORM tool for Java that integrates well with Spring. We discuss the "Mapper" classes MyBatis wants us to design to access the database.
-   **Practical Example - User Credentials and Authentication:**  As a motivating example for using ORM, a discussion was made about how to implement basic login security with a User table, MyBatis, and Spring Security.

## ORM - Object-Relational Impedance Mismatch
PS: RDBMS =  Relational Database Management System

Object-Relational Impedance Mismatch' (sometimes called the 'paradigm mismatch') is just a fancy way of saying that object models and relational models do not work very well together. RDBMSs represent data in a tabular format, whereas object-oriented languages, such as Java, represent it as an interconnected graph of objects.

Loading and storing graphs of objects using a tabular relational database exposes us to 5 mismatch problems:

- **Granularity**: object model which has more classes than the number of corresponding tables in the database (In this case, the object model is more granular than the relational model)
- **Subtypes**: Inheritance is a natural paradigm in object-oriented programming languages. However, RDBMSs do not define anything similar.
- **Identity**: A RDBMS defines exactly one notion of 'sameness': the `primary key`. Java, however, defines both object identity `a==b` and object equality `a.equals(b)`.
- **Associations**: Associations are represented as unidirectional references in Object Oriented languages whereas RDBMSs use the notion of foreign keys. If you need bidirectional relationships in Java,  the association must be defined twice.
- **Data Navigation**: The way you access data in Java is fundamentally different than the way it is done in a relational database. In Java, the navigation is from one association to an other walking the object network. This is not an efficient way of retrieving data from a relational database. In the relational database firstly is required load several entities via JOINs and select the targeted entities before you start walking the object network.

## FYI - JDBC Introduction
JDBC helps you to write Java applications that manage these three programming activities:

1.  Connect to a data source, like a database
2.  Send queries and update statements to the database
3.  Retrieve and process the results received from the database in answer to your query

The following simple code fragment gives a simple example of these three steps:

```java
public void connectToAndQueryDatabase(String username, String password) {

    Connection con = DriverManager.getConnection(
                         "jdbc:myDriver:myDatabase",
                         username,
                         password);

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM Table1");

    while (rs.next()) {
        int x = rs.getInt("a");
        String s = rs.getString("b");
        float f = rs.getFloat("c");
    }
}
```
This short code fragment instantiates a  `DriverManager`  object to connect to a database driver and log into the database, instantiates a  `Statement`  object that carries your SQL language query to the database; instantiates a  `ResultSet`  object that retrieves the results of your query, and executes a simple  `while`  loop, which retrieves and displays those results. It's that simple.

## ORM and Security
![orm-and-security.png](orm-and-security.png)

[MyBatis](https://mybatis.org/mybatis-3/index.html)  _to transform Java objects to SQL query parameters and to transform SQL query results into Java objects._ MyBatis Mappers can be created as Spring beans, so they can be injected into any other beans that need them

![myBatis-in-the-app-context-and-integration.png](myBatis-in-the-app-context-and-integration.png)

### Additional Reading
-   Briefly read the research paper titled  [Object/relational mapping 2008: hibernate and the entity data model](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.457.1205&rep=rep1&type=pdf)  by Elizabeth O'Neil for a better insight into the ORM.
-   If you are not familiar with JDBC, refer to this official  [JDBC Introduction](https://docs.oracle.com/javase/tutorial/jdbc/overview/index.html), and have a quick look at the underlying architecture. Though, we will not use the JDBC directly. MyBatis will automatically generate JDBC requests. You will learn and implement MyBatis shortly.

### What Data Should be Stored in a Database?
-   Data shared across multiple user sessions, like a product inventory
-   Persistent data that should remain accessible after being logged out, like user profile or shopping cart

### How Should Data be Structured?
-   Intuitively. Most data can be stored in a similar format to the data objects that represent it in Java, with attributes matching column names.
-   Differing. Some data must be stored differently for security reasons, such as encrypted passwords. Other data may require a different format for efficient storage, such as large files.

### Thinking about Security
The main question to ask is:  _“What pages can a user access?”_

-   User-Specific Data
-   Generally Accessible (Unsecured) Data
-   May Vary by Domain

## MyBatis Mappers
- [Article MyBatis Annotations — Result Mapping](https://medium.com/@hsvdahiya/mybatis-annotations-result-mapping-spring-79944ff74b84)

MyBatis provides a shallow ORM layer over JDBC (Java Database Connectivity). That means it helps map Java objects to queries that save and retrieve data using JDBC.

MyBatis is mostly used through interface definitions. MyBatis automatically generates classes that implement the interface and makes them available as Spring beans.

```java
@Mapper
public interface UserMapper {
   @Select("SELECT * FROM USERS WHERE username = #{username}")
   User getUser(String username);
}
```

### `@Insert`, `@Update`, and `@Delete`

The  `@Insert`  annotation automatically references attributes on the `user` object. Note username, firstName, lastName are all referenced directly here, because they are attributes of the `user` object.

```java
@Mapper
public interface UserMapper {
   @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) " +
           "VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
   @Options(useGeneratedKeys = true, keyProperty = "userId")
   int insert(User user);
}
```

This example also demonstrates the `@Options` annotation. `@Insert` normally returns an integer that is the count of rows affected. By using the `@Options` annotation, MyBatis has been configured to automatically generate a new key and put it in userId. Now the method will return the new userId once the row has been inserted.

### Further Research
-   For a full list of the available MyBatis annotations and some example usage, see the  [MyBatis Java API documentation](https://mybatis.org/mybatis-3/java-api.html).
-   For an informal overview of result mapping with MyBatis

## User Authentication
### Authentication in a web application
User support is a common feature in web applications, which means that a user can register an account and use credentials to login to the application in the future.

It's important to design databases with the assumption that they will someday be breached, and so passwords or other secret credentials cannot be stored in plain text. Two approaches to storing passwords are:
-   **Encryption**: Modifying data before storing it, with the intention of using another algorithm to return the data to its original form once it needs to be used.
-   **Hashing**: Modifying data before storing it with the intention of never returning it to its original form. The modified data will be compared to other modified data only.

Hashing and Encryption should occur in a service dedicated to that purpose, rather than on the front end or in the controller. Hashing sometimes makes use of another technique, Salting.

- **Salt**: random data that is combined with the input string when hashing so that the resultant hashed values are unique for each row. This means that two users with the same password would not have the same hash in the database.

#### Example of HashService
```java
// HashService
public String getHashedValue(String data, String salt) {
    byte[] hashedValue = null;

    KeySpec spec = new PBEKeySpec(data.toCharArray(), salt.getBytes(), 5000, 128);
    try {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        hashedValue = factory.generateSecret(spec).getEncoded();
    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
        logger.error(e.getMessage());
    }

    return Base64.getEncoder().encodeToString(hashedValue);
}
```

### Spring `AuthenticationProvider` - Authentication Implementation 
Spring Security provides a variety of options for performing authentication. These follow a simple contract – **an  _Authentication_  request is processed by an  _AuthenticationProvider_** and a fully authenticated object with full credentials is returned.

When a user logs in, there is no way to retrieve their original password, but it is possible to re-hash their user input and see if it matches the hashed value in the database.

Below there is an example of a service called `AuthenticationService` that implements the Spring Interface `AuthenticationProvider`. As said in the first paragraph, this interface is used by spring to perform authentication using a pré-defined **authentication schema**.

The `authenticate()` method takes an Authentication object from spring and returns an authentication token if the user's credentials are correct.

The `supports()` method return all the available **authentication schema** supported by the current version of the application. 

In the example below the `UsernamePasswordAuthenticationToken` is being used as the authentication schema of the system.

```java
@Service
public class AuthenticationService implements AuthenticationProvider {
    private UserMapper userMapper; // A repository 
    private HashService hashService; // A basic service to hash passwords

    public AuthenticationService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userMapper.getUser(username);
        if (user != null) {
            String encodedSalt = user.getSalt();
            String hashedPassword = hashService.getHashedValue(password, encodedSalt);
            if (user.getPassword().equals(hashedPassword)) {
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
```

### Spring `EnableWebSecurity`
In order for Spring to actually use the `AuthenticationService` defined above, Spring must be configured. It is done by extending the `WebSecurityConfigurerAdapter`.

This example overrides two configure methods:
-   **configure(AuthenticationManagerBuilder auth)**: used to tell Spring to use the  `AuthenticationService`  to check user logins
-   **configure(HttpSecurity http)**: used to configure the  `HttpSecurity object`  by chaining methods to express security requirements

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // (*!*!*!)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.authenticationService);
    }

    // (******)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/signup", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .permitAll();

        http.formLogin()
                .defaultSuccessUrl("/home", true);
    }
}
```

We can see that the second ((******))  `configure`  method does four things:
-   Allows all users to access the /signup page, as well as the css and js files.
-   Allows authenticated users to make any request that's not explicitly covered elsewhere.
-   Generates a login form at `/login` and allows anyone to access it.
-   Redirects successful logins to the `/home` page.