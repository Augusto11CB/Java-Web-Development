package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.CredentialFormDTO;
import aug.bueno.cloudstorage.dto.FileFormDTO;
import aug.bueno.cloudstorage.dto.NoteFormDTO;
import aug.bueno.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/*
 * - [X]Any errors related to file actions should be displayed. For example, a user should not be able to upload two files with the same name, but they'll never know unless you tell them!
 *
 * - [X]The application should not allow duplicate usernames or duplicate filenames attributed to a single user.
 *
 * - [X]A logged-in user should only be able to view their own data, and not anyone else's data. The data should only be viewable to the specific user who owns it.
 *
 * - [X]When a user logs in, they should see the data they have added to the application.
 *
 */

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;
    private EncryptionService encryptionService;

    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService,
                          FileService fileService, EncryptionService encryptionService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(
            final Authentication auth,
            final Model model,
            @ModelAttribute("noteForm") final NoteFormDTO noteFormDTO,
            @ModelAttribute("credentialForm") final CredentialFormDTO credentialFormDTO,
            @ModelAttribute("fileForm") final FileFormDTO fileFormDTO
    ) {
        int userID = userService.findUserByUserName(auth.getName()).get().getUserID();

        List<NoteFormDTO> noteFormDTOS = noteService.findAllNotesUser(userID);
        List<CredentialFormDTO> credentialFormDTOS = credentialService.findAllCredentialsUser(userID);
        List<FileFormDTO> fileFormDTOSDTOS = fileService.findAllUserFiles(userID);

        model.addAttribute("notes", noteFormDTOS);
        model.addAttribute("credentials", credentialFormDTOS);
        model.addAttribute("files", fileFormDTOSDTOS);
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

}
