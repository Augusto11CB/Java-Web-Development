# Security
### Authentication
Authentication is about validating the identity of a client attempting to call a web service. Typically, identity is validated with user credentials, such as a user name and password.

### Authorization
Authorization is the next step after authentication. So once a client is authenticated (they have proven who they are), what do they have access to? For example, what data can they view, are they allowed to change that data, etc.

### Basic Authentication
Basic Authentication (also referred to as Basic Auth) is the simplest protocol available for performing web service authentication over HTTP protocol. Basic Auth requires a **username and password**. The client calling the web service takes these two credentials, converts them to a single  [Base 64–encoded value](https://en.wikipedia.org/wiki/Base64)  and passes it along in the Authentication HTTP header.

The server compares the credentials passed to those stored. If it matches, the server fulfills the request and provides access to the data. If the Authentication HTTP header is missing or the password doesn’t match the user name, the server denies access and returns a  [401 status code](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes), which means the request is Unauthorized.

### API Key Authentication
 API Key Authentication is another technique used to secure Web Services. It requires the API to be accessed with a unique key. 

## Spring Security
[Spring Security](https://spring.io/projects/spring-security) is a part of the Spring Framework and provides authentication, authorization and other security features for Spring-based applications.

**@EnableWebSecurity**: Annotation that enables Spring Security’s support

