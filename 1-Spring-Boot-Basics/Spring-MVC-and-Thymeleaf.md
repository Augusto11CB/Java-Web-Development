# Spring MVC and Thymeleaf
## Developing Web Sites from the Back-end
![relationship-between-spring-mvc-and-client.png](relationship-between-spring-mvc-and-client.png)

The figure above shows the relationship between Spring MVC and the client. Spring MVC uses HTML templates and application data to render a view for the client browser to display. The view exposes certain actions to the user, which when triggered are sent to Spring MVC, which processes the actions and renders new views for the client.

### Interacting with a simple web application
In a web application, there are two components: the client that sends HTTP requests, and the server, which sends HTTP responses back. In the case of a web browser client, the responses the server sends need to be in the format of HTML, the document language of the web. The HTML that is sent to the client both defines the data that the user sees, as well as the actions a user can take - things like buttons, links, and input forms are all part of what the server is responsible for generating.

### HTML templates
_HTML templates are essentially just HTML files with special tags and attributes that can be combined with a data model by a template engine like Thymeleaf to generate fully functional HTML documents._  Spring MVC provides a standard way to choose a template and supply the necessary data model when a request comes in, which allows for a truly dynamic user experience.

> Data model defines how the application stores and retrieves (serialize and deserialize) the Java objects into/from the database. Data modeling starts with designing the database tables which are analogous to the Java classes ("model" classes of the MVC).

-   [Tutorial: Thymeleaf + Spring](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)


## Servers, Templates and MVC
![web-basic-interactions.png](web-basic-interactions.png)

### Description of the interaction between the Web Server, the Spring Controller, and the HTML template
The web server receives an HTTP request and uses it to call a method in the Spring controller. The Spring controller populates a model object and returns a String with the view id. The template corresponding to the view id is populated with data from the model object.


## Understanding Spring MVC
![spring-mvc-structure.png](spring-mvc-structure.png)

The figure above shows the Spring MVC's architecture. The **browser represents the view,** and requests from the browser are user actions. When **Spring MVC processes a request, it creates a `Model`** object that represents the dynamic data associated with the view and passes it to a controller method that matches the request. T**he controller updates the model and chooses a template to render in response**. **Spring MVC passes the template and the updated model to Thymeleaf**, which generates an updated view, which Spring sends in response to the browser.

MVC is an acronym that stands for Model-View-Controller, and it's a common software pattern for user interface design. Traditionally, it divides the roles of components in an app into three layers:

-   the  **Model**, which is responsible for maintaining the state of an application,
-   the  **View**, which is responsible for displaying the UI to the user,
-   and the  **Controller**, which is responsible for processing user actions (sent from the View) to update the Model, and for forwarding those updates back to the View

MVC is an abstract pattern, though, and every library implements it differently. Spring MVC is built around the browser as a platform, and it organizes these roles like this:

-   **HTML templates**  are the views - each one represents a specific screen or screen component that the user is shown.
-   **Spring beans**  are the controllers - specifically, Spring MVC gives an  `@Controller`  annotation that we can use to register our beans as controllers. Think of Spring bean controllers as specialized application components that can define methods to handle specific user requests. Those methods are responsible for choosing the HTML template that is generated in response, as well as for populating the  `Model`  object for that template.
-   **`Model`  objects**  are the models - every controller method can take an optional  `Model`  argument, and by reading and changing the data inside of it, the controller can read user-submitted data and populate the template with the changes. Think of the Model class a simple data-transfer object: something that can store various bits of data with keys to look that data up, and that can be passed between the browser, the template engine, and the controller to facilitate the transfer of data between the user and the application.

### Returning a HTML Template Using Spring MVC![returning-a-html-template-using-spring-mvc.png](returning-a-html-template-using-spring-mvc.png)

In order to actually render dynamic data in a template, we again need to approach it from both the template and the controller.

In the template, we need to add Thymeleaf  _attributes_  to our HTML. In our example so far, we added the  `th:text`  attribute to the heading we want to be dynamic, like so:
```html
<h1 th:text="${welcomeMessage}">Hello, homepage!</h1>
``` 
This attribute will cause Thymeleaf to replace the text inside the  `h1`  tag (`Hello, homepage!`) with a string generated by evaluating the expression in the  `th:text`  attribute (`${welcomeMessage}`). The syntax of this expression is fairly simple: the  `${}`  indicates an expression to evaluate, and by using a  _name_  like  `welcomeMessage`  inside of it, we're telling Thymeleaf to look up a value in the model supplied for this template with the same name.

For that to work, though, we need to add a value named  `welcomeMessage`  to the model - and we do that in the controller method like so:
```java
    @RequestMapping("/home")
    public String getHomePage(Model model) {
        model.addAttribute("welcomeMessage", "Hi Hello");
        return "home";
    }
```

First we need to add an argument to the controller method - the  `Model`  object you see above. This is a special class that Spring MVC will send to Thymeleaf to render the template, and we can set various  _attributes_  on it to add named values. As you can see, we're adding a value of  `"Hi Hello"`  to the model with the name  `"welcomeMessage"`  - which is exactly the name we're referencing in our template! Now when we render the template, the message  `Hi Hello`  will appear on the web page instead of  `Hello, homepage!`

### Key Terms
-   **MVC**: MVC is an acronym that stands for  **M**odel-**V**iew-**C**ontroller, and it's a common software pattern for user interface design
-   **Model**: in MVC, the Model is responsible for maintaining the state of an application,
-   **View**: in MVC, the View is responsible for displaying the UI to the user,
-   **Controller**: in MVC, the Controller is responsible for processing user actions (sent from the View) to update the Model, and for forwarding those updates back to the View
-   **Template**: In software development, templates are used in many different contexts - in general, they are a way to define some often-repeated or reused text or code in a specific format, like HTML, along with  _code hooks_  that indicate portions of the template that should be replaced dynamically when the template is rendered. In our context, we mostly use Thymeleaf's  _HTML templates_, which mostly look like plain HTML with a few extra Thymeleaf-specific attributes. These attributes are our  _code hooks_, and allow us to define what data Thymeleaf uses when generating the final HTML from our template.

> MVC and patterns like it can be found throughout the software world. All the scenarios below Model-View-Controller design can be applied.
> -   A browser communicates with a web server, which updates information in a database and returns a response to the browser.  
> -   An operating system uses a file explorer to present the contents of its file system to users.
> -   A mobile game provides a touch-screen interface to communicate with a game server that updates game state in memory on the server. 
> -   A phone system that allows users to submit credit card payments through an automated menu.    
> -   A waitperson taking a customer order and relaying it to the kitchen, where food is then prepared and brought back to the customer.

### Further Research
-   [Getting Started - Tutorial on Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
-   [High-level Overview of Different MVC Variants](https://uniandes-se4ma.gitlab.io/books/chapter8/mvc-mvvm-mv-mvwhat.html)

## HTML Templates
The nature of Spring MVC's data flow - the controller has to provide the data for the template it wants to render - means that often, when designing a new user-facing feature, it makes sense to start with the template.

Thymeleaf is the HTML  _template engine_ , a library that can take a template, a data model, and combine them to generate a final HTML document.

The way that Thymeleaf applies the data model to the template is based on the Thymeleaf  _attributes_   added to the template, like  `th:text` . These attributes can have complex expressions as their values, which are processed by Thymeleaf in the context of the data model provided.

```html
<h1 th:each="msg : ${greetings}">
	th:text="${msg}"
	th:unless="${msg.contains('goodbye')}"
> Hello, homepage! </h1>
```

In addition to setting text values, we can use Thymeleaf attributes to perform basic  _control flow_  in our templates. The main two types of control flow in templates are iteration and conditional rendering.

### Iteration 
**Iteration**  in Thymeleaf templates works very similarly to an enhanced for-loop in Java. The main attribute used in template iteration is  `th:each`, and its use looks like this:

```
<h1  th:each="msg : ${greetings}" th:text="${msg}">Hello, homepage!</h1>
```

We can read the attribute  `th:each="msg : ${greetings}"`  as "for each  `msg`  in  `greetings`", and it essentially means "repeatedly render this element for each value in the  `greetings`  collection, and name that element  `msg`  for any expressions that need that name to have a value associated with it.

in this example, the  `h1`  tag will be repeatedly rendered, once for each element in  `greetings`, and the text rendered by  `th:text="${msg}"`  will be the current element of greetings being rendered. If  `greetings`  contained the Strings "Hi" and "Hello", the final rendered html would be

```html
<h1>Hi</h1>
<h1>Hello</h1>
```

### Conditional Rendering
**Conditional rendering**  in Thymeleaf usually looks like an if statement in Java, or occasionally a switch statement. The attribute we examined in the example was  `th:unless`, as in:

```
<h1  th:unless="${msg.contains('goodbye')}" th:text="${msg}">Hello, homepage!</h1>
```

In this case, if the  `msg`  contains the String  `"goodbye"`, the  `h1`  tag  **will not**  be rendered. This is particularly useful when choosing whether or not to render an error message, for example. If you need the opposite condition, in this case  _only_  showing messages that contain  `"goodbye"`, you can simply replace the  `th:unless`  with  `th:if`.

### `th:each="abc : ${xyz}`
```
<tbody>
    <tr th:each="cat : ${cats}">
        <td th:text="${cat.color}" />
        <td th:text="${cat.maxSpeedMph}" />
    </tr>
</tbody>
```

### Concatenate Strings Inside Thymeleaf attribuite
```
<p th:text=”${cat.name + ‘ is ‘ + cat.color}”></p>
```

### `th:if` 
```
<p th:if="${cat.maxSpeedMph > 10}">Pretty fast</p>
<p th:unless="${cat.maxSpeedMph >= 10}">Not as fast</p>
```

### `th:action`, `@{}`,`th:object` and `*{}`
```
<form action="#" th:action="@{'/cat'}" th:object="${cat}" method="POST">
    <label for="newCatText">Name your cat: </label>
    <input type="text" id="newCatName" name="newCatName" th:field="*{name}">
    <input type="submit">
</form>
```

- `th:field="*{name}"`: It is a way to reference an attribute of the cat object we specified at the top of the form. This form will allow us to submit a cat object with the name provided to the `/cat` endpoint.

- `th:object`: It allows us to specify an object whose attributes can be referenced in the scope below. Another definition can be "to choose the POJO our form data should be added to".

### `th:text` vs `th:field`
- `<p th:field="${abcd}">`: It identifies form fields where user input will be mapped to variables
- `<p th:text="${abcd}">`: **replaces the text** of its element with the value of the variable that was provided.

### `${...}` ,`*{...}`, `#{...}`, `@{...}` and `~{...}`
-   `${...}`  : Variable expressions. These are OGNL expressions (or Spring EL if you have spring integrated)
    
-   `*{...}`  : Selection expressions. Same as above, excepted it will be executed on a previously selected object only
    
-   `#{...}`  : Message (i18n) expressions. Used to retrieve locale-specific messages from external sources
    
-   `@{...}`  : Link (URL) expressions. Used to build URLs
-   `~{...}`  : Fragment expressions. Represent fragments of markup and move them around templates

### `th:with`, `th:block`, [`th:switch` and `th:case`]
```html
<div th:each="chatMessage : ${chatMessages}">
    <div>
        <!-- th:with : creates a local variable -->
        <span th:with="messageWords=${#strings.listSplit(chatMessage.message,' ')}" th:text="${messageWords}"></span>
        <!-- th:block is a mere attribute container that allows template developers to specify whichever attributes they want. Thymeleaf will execute these attributes and then simply make the block, but not its contents, disappear. -->
        <th:block th:switch="${chatMessage.messageType}">
            <div th:case="'Say'">
                <span th:text="${chatMessage.userName}">Username</span>: <span th:text="${chatMessage.message}">No Messages</span>
            </div>
            <div th:case="'Shout'">
                <span th:text="${chatMessage.userName}">Username</span>: <b><span th:text="${chatMessage.message}">No Messages</span></b>
            </div>
            <div th:case="'Whisper'">
                <span th:text="${chatMessage.userName}">Username</span>: <i><span th:text="${chatMessage.message}">No Messages</span></i>
            </div>
        </th:block>
    </div>
</div>
```

- `th:with`: creates a local variable
- `th:block`: is a mere attribute container that allows template developers to specify whichever attributes they want. Thymeleaf will execute these attributes and then simply make the block, but not its contents, disappear.

### `th:href`
The **th:href** is an attribute of the anchor tag, which is also used to identify sections within a document. The **th:href** contains two components: the URL, which is the actual link, and the clickable text that appears on the page, called the "anchor text."

There are different types of URLs:

-   Absolute URLs:  `http://www.thymeleaf.org`
-   Relative URLs, which can be:
    -   Page-relative:  `user/login.html`
    -   Context-relative:  `/itemdetails?id=3`  (context name in server will be added automatically)
    -   Server-relative:  `~/billing/processInvoice`  (allows calling URLs in another context (= application) in the same server.
    -   Protocol-relative URLs:  `//code.jquery.com/jquery-2.0.3.min.js`

```html
<!-- Will produce 'http://localhost:8080/gtvg/order/details?orderId=3' (plus rewriting) -->
<a href="details.html" 
   th:href="@{http://localhost:8080/gtvg/order/details(orderId=${o.id})}">view</a>

<!-- Will produce '/gtvg/order/details?orderId=3' (plus rewriting) -->
<a href="details.html" th:href="@{/order/details(orderId=${o.id})}">view</a>

<!-- Will produce '/gtvg/order/3/details' (plus rewriting) -->
<a href="details.html" th:href="@{/order/{orderId}/details(orderId=${o.id})}">view</a>
```
-   If several parameters are needed, these will be separated by commas:  `@{/order/process(execId=${execId},execType='FAST')}`
-   Variable templates are also allowed in URL paths:  `@{/order/{orderId}/details(orderId=${orderId})}`

As was the case with the message syntax (`#{...}`), URL bases can also be the result of evaluating another expression:

```html
<a th:href="@{${url}(orderId=${o.id})}">view</a>
<a th:href="@{'/details/'+${user.login}(orderId=${o.id})}">view</a>
```

### Further Reading
-   [The official Thymeleaf tutorial](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#introducing-thymeleaf), which explains the entire framework from first principles.
-   [The official Thymeleaf expression syntax tutorial](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#standard-expression-syntax)  - Read this section if all those  `${}`s aren't making much sense.
-   [The official Spring Expression Language docs](https://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html)  - This will tell you how to perform various computations inside of the Thymeleaf expression brackets

## Connecting Controllers to Templates - Handling User-Submitted Data
In order to handle user-submitted data, there are two main components to this, one on the **template side**, the other on the **controller side**.

On the **template side**, for each piece of data that should be captured input fields have to be defined, as well as a  `<form>`  element to group them. **When the form is submitted, the data will be encoded in the HTTP request that is sent, and can be extracted on the Spring side.**

On the Spring side of things, a POJO to hold the form data also need to be defined. We'll look at code details in the next video, but by defining this POJO, it is possible to pre-fill the form by setting its fields and adding it as a Model attribute when first rendering the template, and Spring can automatically extract the request data into that POJO when the form is submitted.

### Example 
#### Template
```html
<form th:object="${newMessage}" th:action="@{/home}" action="#" method="POST">
    <input th:field="*{text}" type="text">
    <input type="submit">
</form>
```

#### Controller
```java
	@GetMapping("/home")
    public String getHomePage(@ModelAttribute("newMessage") MessageForm newMessage, Model model) {
        model.addAttribute("greetings", this.messageListService.getMessages());
        return "home";
    }
	@PostMapping("/home")
    public String addMessage(@ModelAttribute("newMessage") MessageForm messageForm, Model model) {
        messageListService.addMessage(messageForm.getText());
        model.addAttribute("greetings", messageListService.getMessages());
        messageForm.setText("");
        return "home";
    }
		
	// Annotation that binds a method parameter or method return value  
	// to a named model attribute, exposed to a web view.
	@ModelAttribute("allMessageTypes")  
	public String[] allMessageTypes() {  
	    return new String[]{"Comercial", "Advice", "Extra Content"};  
	}
```

The key elements to focus on are the new arguments to each of these methods - the `MessageForm` class is a POJO specifically designed to hold the form data.

For the `GET` request handling method, the `MessageForm`  was declared as an argument to ensure that the object exists and is added to the model by Spring automatically. Even if there isn't any data in the object yet, this is necessary, because **Thymeleaf needs an object with the name `newMessage` to be present in the model to _render properly_**.

For the `POST` request handling method, the `MessageForm`  was declared as an argument to tell Spring that it should look for data that matches that in the body of the request we're handling. Spring will then automatically extract that data and put it in a `MessageForm` object before calling the `POST` method, passing it to us so we can use the data as we see fit.

> In both cases, we're annotating this argument with `@ModelAttribute`. This allows us to specify that Spring should add the object to our `Model` before asking Thymeleaf to render the template. That means we don't have to add it manually.

### Exercise - Connecting Controllers to Templates
1.  Add an action to the form that directs it to the endpoint  `/animal`  using a POST request type and binds the form data to an object called  `messageForm`.
2.  Bind the input text to two fields called  `animalName`  and  `adjective`.
3.  Display the contents of the list  `greetings`  in the first  `<h1>`  element. You should display all the elements in the list.
4.  Use conditional logic to only show the list of greetings if there are 5 or fewer messages. Otherwise, show the message ‘I think that’s enough!’

```html
<body>
<form action="#" th:action="@{'/animal'}" th:object="${messageForm}" method="POST">
   <label for="animalText">Enter an Animal: </label>
   <input type="text" id="animalText" name="animalText" th:field="*{animalName}">
   <label for="adjective">Enter an Adjective:</label>
   <input type="text" id="adjective" name="adjective" th:field="*{adjective}">
   <input type="submit">
</form>
   <h1 th:unless="${#lists.size(greetings) > 5}" th:each="msg : ${greetings}" th:text="${msg}">Hello, homepage!</h1>
   <h1 th:if="${#lists.size(greetings) > 5}">I think that's enough!</h1>
</body>
```

### Key Terms
- **Form-Backing Object**: This is a term used by Spring MVC and Thymeleaf to mean an object that represents the data contained in a form. On the Spring side, this is usually an additional argument to the relevant Controller method, and on the Thymeleaf side, this is referred to in the  `th:object`  attribute on the form.
- `@ModelAttribute`: Annotation that binds a method parameter or method return value to a named model attribute, exposed to a web view.

### Further Research
-   [Official Spring MVC docs with some discussion of form-backing objects](https://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/mvc.html).
-   [Official Thymeleaf docs for writing forms and integrating with Spring](https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#creating-a-form).

## File Upload Requests over HTTP
In order to handle file upload requests over HTTP there is a special class provided by Spring. `MultiPartFile`, a special request data class provided by Spring to handle large file uploads. As with any special data type, simply by adding it as an argument to our controller method, Spring knows to treat the incoming request appropriately.

On the Thymeleaf side of things, an `ectype` must be defined. It can be done by setting up a form for file upload using the `enctype="multipart/form-data"` attribute.

### Template
```
<form action="#" enctype="multipart/form-data" th:action="@{'/file-upload'}" method="POST">
   <input type="file" class="form-control-file" id="fileUpload" name="fileUpload">
   <input type="submit">
</form>
```

### Controller
```java
@PostMapping("file-upload")
public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model) {
	InputStream fis = fileUpload.getInputStream():
}
```

### Further Research
-   [Official Spring MultiPartFile Javadocs](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html)
-   [Official Spring MVC file upload tutorial](https://spring.io/guides/gs/uploading-files/)




