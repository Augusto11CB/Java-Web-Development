package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.SignupFormDTO;
import aug.bueno.cloudstorage.model.User;
import aug.bueno.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

// An error message is already present in the template, but should only be visible if an error occurred during signup.

// The application should not allow duplicate usernames or duplicate filenames attributed to a single user.

@Controller
@RequestMapping("/signup")
public class SignupController {

    private UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage(final @ModelAttribute("signupForm") SignupFormDTO signupForm, final Model model) {
        return "signup";
    }

    @PostMapping
    public String signupNewUser(final @ModelAttribute("signupForm") SignupFormDTO signupForm, final Model model) {

        if (this.isSubmittedSignupDataValid(signupForm)) {
            Optional<User> user = userService.createUser(signupForm.getUserName(), signupForm.getPassword(), signupForm.getFirstName(), signupForm.getLastName());

            if (user.isPresent()) {
                model.addAttribute("signupWithSuccess", true);
            } else {
                model.addAttribute("signupError", "There was an error signing you up. Please try again.");
            }
        } else {
            model.addAttribute("signupError", "The username already exists.");
        }

        return "signup";
    }

    private boolean isSubmittedSignupDataValid(final SignupFormDTO signupForm) {
        // TODO - Validate signup form
        return false;
    }
}
