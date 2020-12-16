package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.NoteFormDTO;
import aug.bueno.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

// Any errors related to file actions should be displayed. For example, a user should not be able to upload two files with the same name, but they'll never know unless you tell them!

// The application should not allow duplicate usernames or duplicate filenames attributed to a single user.

// A logged-in user should only be able to view their own data, and not anyone else's data. The data should only be viewable to the specific user who owns it.

// When a user logs in, they should see the data they have added to the application.

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;

    public HomeController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public String getHomePage(final Model model, @ModelAttribute("noteForm") NoteFormDTO noteFormDTO) {
        List<NoteFormDTO> noteFormDTOS = noteService.findAllNotesUser(1).stream().map(note -> {
            return NoteFormDTO.builder()
                    .noteDescription(note.getNoteDescription())
                    .noteTitle(note.getNoteTitle())
                    .noteID(note.getNoteID())
                    .userID(note.getUserID())
                    .build();
        }).collect(Collectors.toList());

        model.addAttribute("notes", noteFormDTOS);

        return "home";
    }

}
