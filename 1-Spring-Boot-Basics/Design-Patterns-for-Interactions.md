# Design Patterns for Interactions
Consider the following interactions. They’re all valid design patterns

-   The model data is observable, and the subscribers receive updates of state change events.
	- This pattern might be more appropriate for a desktop client app, or an application requiring high responsiveness.
    
-   All user actions and view updates are exchanged as HTTP request and response objects. The controller is responsible for choosing templates and populating the model data.
    
-   One component retrieves data and processes it, another component formats the visual presentation data, and the controller handles the flow between the first two components.
	- This pattern can be useful for programs that need to parallelize many actions at once
    
-   The controller is the master of both the model and the view. All interactions are defined by the controller, which updates the model and returns the view as necessary.
	- This pattern can describe an API-based form of MVC, but doesn’t quite fit for Spring.