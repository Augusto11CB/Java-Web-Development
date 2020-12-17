package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.CredentialFormDTO;
import aug.bueno.cloudstorage.services.CredentialService;
import aug.bueno.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * Credentials
 * <p>
 * - [X] Creation: On successful credential creation, the user should be shown a success message and the created credential should appear in the list.
 * <p>
 * - [X] Edit/Update: When a user selects update, they should be shown a view with the unencrypted credentials. When they select save, the list should be updated with the edited credential details.
 * <p>
 * - [X] Deletion: On successful credential deletion, the user should be shown a success message and the deleted credential should disappear from the list.
 * <p>
 * - [X] Errors: Users should be notified of errors if they occur.
 */

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private Logger LOGGER = LoggerFactory.getLogger(CredentialController.class);

    private CredentialService credenialService;
    private UserService userService;

    public CredentialController(CredentialService credenialService, UserService userService) {
        this.credenialService = credenialService;
        this.userService = userService;
    }

    @PostMapping()
    public String insertUpdateNewCredential(ModelMap model, @ModelAttribute("credentialForm") CredentialFormDTO credentialFormDTO) {
        LOGGER.info(credentialFormDTO.toString());

        boolean result = credenialService.insertOrUpdateCredential(credentialFormDTO, 1);

        return result ? "redirect:/result?isSuccess=" + result : "redirect:/result?error=" + true;
    }

    @GetMapping("/delete")
    public String deleteCredential(
            ModelMap model,
            @ModelAttribute("credentialForm") CredentialFormDTO credentialFormDTO,
            @RequestParam("id") Integer credentialID
    ) {
        LOGGER.info(credentialID.toString());

        boolean result = false;

        try {

            if (credentialID > 0) {
                result = credenialService.deleteByCredentialID(credentialID);
            }

            return result ? "redirect:/result?isSuccess=" + true : "redirect:/result?error=" + true;

        } catch (Exception e) {
            this.LOGGER.error(e.getMessage());
            return "redirect:/result?error=" + false;
        }
    }

}

