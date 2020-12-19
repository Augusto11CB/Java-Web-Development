package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.NoteFormDTO;
import aug.bueno.cloudstorage.services.NoteService;
import aug.bueno.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/*
 * Note
 * <p>
 * - [X] Creation: On successful note creation, the user should be shown a success message and the created note should appear in the list.
 * <p>
 * - [X] Deletion: On successful note deletion, the user should be shown a success message and the deleted note should disappear from the list.
 * <p>
 * - [X] Edit/Update: When a user selects edit, they should be shown a view with the note's current title and text. On successful note update, the user should be shown a success message and the updated note should appear from the list.
 * <p>
 * - [X] Errors: Users should be notified of errors if they occur.
 */

@Controller
@RequestMapping("/note")
public class NoteController {
    private Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping()
    public String insertOrUpdateNote(
            final Authentication auth,
            final ModelMap model,
            @ModelAttribute("noteForm") final NoteFormDTO noteForm
    ) {
        LOGGER.info(noteForm.toString(), auth.getName());

        int userID = userService.findUserByUserName(auth.getName()).get().getUserID();

        boolean result = noteService.insertOrUpdateNote(noteForm, userID);
        return result ? "redirect:/result?isSuccess=" + true : "redirect:/result?error=" + true;
    }

    @GetMapping("/delete")
    public String deleteNote(
            final ModelMap model,
            final @ModelAttribute("noteForm") NoteFormDTO noteFormDTO,
            final @RequestParam("id") Integer noteID
    ) {
        LOGGER.info(noteID.toString());

        boolean result = false;

        try {
            if (noteID > 0) {
                result = noteService.deleteByNoteID(noteID);
            }

            return result ? "redirect:/result?isSuccess=" + true : "redirect:/result?error=" + true;

        } catch (Exception e) {
            this.LOGGER.error(e.getMessage());
            return "redirect:/result?error=" + false;
        }
    }

}
