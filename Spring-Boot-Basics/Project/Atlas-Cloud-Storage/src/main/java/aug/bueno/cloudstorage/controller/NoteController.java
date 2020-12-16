package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.NoteFormDTO;
import aug.bueno.cloudstorage.services.NoteService;
import aug.bueno.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 Note

 - Creation: On successful note creation, the user should be shown a success message and the created note should appear in the list.

 - Deletion: On successful note deletion, the user should be shown a success message and the deleted note should disappear from the list.

 - Edit/Update: When a user selects edit, they should be shown a view with the note's current title and text. On successful note update, the user should be shown a success message and the updated note should appear from the list.

 - Errors: Users should be notified of errors if they occur.

 */


@Controller
@RequestMapping("/note")
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping()
    public String insertNewNote(Model model, @ModelAttribute("noteForm") NoteFormDTO noteForm) {

        // userService.findUserByUserName(); // TODO
        noteService.insertOrUpdateNote(noteForm, 1);
        model.addAttribute("changesSavedWithSuccess", true);

        return "result";
        // return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteNote(
            Model model,
            @ModelAttribute("noteForm") NoteFormDTO noteFormDTO,
            @RequestParam("id") Integer noteID
    ) {

        boolean result = false;
        if (noteID > 0) {
            result = noteService.deleteByNoteID(noteID);
        }

        model.addAttribute("changesSavedWithSuccess", result);
        return "result";
        // return "redirect:/result?error";
    }

}
