package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.SignupFormDTO;
import aug.bueno.cloudstorage.model.User;
import aug.bueno.cloudstorage.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        Optional<String> invalidResult = this.isSubmittedSignupDataValid(signupForm);

        if (invalidResult.isPresent()) {
            model.addAttribute("signupError", invalidResult.get());

        } else {

            Optional<User> user = userService.createUser(signupForm.getUserName(), signupForm.getPassword(), signupForm.getFirstName(), signupForm.getLastName());

            if (user.isPresent()) {
                model.addAttribute("signupWithSuccess", true);
            } else {
                model.addAttribute("signupError", "There was an error signing you up. Please try again.");
            }
        }

        return "signup";
    }

    private Optional<String> isSubmittedSignupDataValid(final SignupFormDTO signupForm) {

        Optional<String> result = Optional.empty();

        result = validateIfRequiredInfoIsPresent(signupForm);

        if (result.isPresent()) {
            return result;
        }

        result = validateIfThereIsDuplicatedUserName(signupForm);

        return result;
    }

    private Optional<String> validateIfThereIsDuplicatedUserName(SignupFormDTO signupForm) {

        Optional<User> userByUserName = userService.findUserByUserName(signupForm.getUserName());

        if (userByUserName.isPresent()) {
            return Optional.of("The username already exists.");
        }

        return Optional.empty();
    }

    private Optional<String> validateIfRequiredInfoIsPresent(SignupFormDTO signupForm) {

        Optional<String> s = Optional.of("There are fields with invalid information, please review your registration");

        if (!StringUtils.isNotBlank(signupForm.getUserName()) ||
                !StringUtils.isNotBlank(signupForm.getPassword()) ||
                !StringUtils.isNotBlank(signupForm.getFirstName()) ||
                !StringUtils.isNotBlank(signupForm.getLastName())
        ) {
            return s;
        }

        return Optional.empty();
    }
}
