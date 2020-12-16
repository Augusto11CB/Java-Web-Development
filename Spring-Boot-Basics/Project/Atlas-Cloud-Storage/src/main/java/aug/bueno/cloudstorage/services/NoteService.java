package aug.bueno.cloudstorage.services;

import aug.bueno.cloudstorage.dto.NoteFormDTO;
import aug.bueno.cloudstorage.model.Note;
import aug.bueno.cloudstorage.repository.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    private List<Note> tempNote;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<NoteFormDTO> findNoteByTitleAndUserID(final int userID, final String noteTitle) {
        return noteMapper.getNoteByTitleAndUserID(noteTitle, userID)
                .stream()
                .map(this::noteToNoteFormDTO)
                .collect(Collectors.toList());
    }

    public List<NoteFormDTO> findAllNotesUser(final int userID) {
        return noteMapper.getAllNotesByUser(userID).stream()
                .map(this::noteToNoteFormDTO)
                .collect(Collectors.toList());
    }

    public void insertOrUpdateNote(final NoteFormDTO noteFormDTO, final int userID) {

        final Note.NoteBuilder noteBuilder = Note.builder()
                .noteDescription(noteFormDTO.getNoteDescription())
                .noteTitle(noteFormDTO.getNoteTitle())
                .userID(userID);
        if (noteFormDTO.getNoteID() == null || noteFormDTO.getNoteID().toString().equals("") || noteFormDTO.getNoteID() <= 0) {
            this.noteMapper.insert(noteBuilder.build());
        } else {
            noteBuilder.noteID(noteFormDTO.getNoteID());
            this.noteMapper.update(noteBuilder.build());
        }
    }

    public boolean deleteByNoteIDAndUserID(final int noteID, final int userID) {
        noteMapper.deleteByNoteIDAndUserID(noteID, userID);
        return true;
    }

    public boolean deleteByNoteID(final int noteID) {
        noteMapper.delete(noteID);
        return true;
    }

    private NoteFormDTO noteToNoteFormDTO(final Note note) {
        return NoteFormDTO.builder()
                .noteID(note.getNoteID())
                .noteTitle(note.getNoteTitle())
                .noteDescription(note.getNoteDescription())
                .userID(note.getUserID())
                .build();
    }

    @PostConstruct
    private void createTestData() {

        this.tempNote = new ArrayList<>();

        tempNote.add(new Note(1, "1", "desc1", 1));
        tempNote.add(new Note(2, "2", "desc2", 1));
        tempNote.add(new Note(3, "3", "desc3", 1));
        tempNote.add(new Note(4, "4", "desc4", 2));
    }
}