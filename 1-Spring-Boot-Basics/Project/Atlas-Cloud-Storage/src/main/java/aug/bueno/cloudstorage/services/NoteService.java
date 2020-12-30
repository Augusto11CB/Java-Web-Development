package aug.bueno.cloudstorage.services;

import aug.bueno.cloudstorage.dto.NoteFormDTO;
import aug.bueno.cloudstorage.model.Note;
import aug.bueno.cloudstorage.repository.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private NoteMapper noteMapper;

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

    public boolean insertOrUpdateNote(final NoteFormDTO noteFormDTO, final int userID) {

        final Note.NoteBuilder noteBuilder = Note.builder()
                .noteDescription(noteFormDTO.getNoteDescription())
                .noteTitle(noteFormDTO.getNoteTitle())
                .userID(userID);

        if (noteFormDTO.getNoteID() == null || noteFormDTO.getNoteID().toString().equals("") || noteFormDTO.getNoteID() <= 0) {
            return this.noteMapper.insert(noteBuilder.build()) > 0;
        } else {
            noteBuilder.noteID(noteFormDTO.getNoteID());
            return this.noteMapper.update(noteBuilder.build()) > 0;
        }
    }

    public boolean deleteByNoteIDAndUserID(final int noteID, final int userID) {
        return noteMapper.deleteByNoteIDAndUserID(noteID, userID);
    }

    public boolean deleteByNoteID(final int noteID) {
        return noteMapper.delete(noteID);
    }

    private NoteFormDTO noteToNoteFormDTO(final Note note) {
        return NoteFormDTO.builder()
                .noteID(note.getNoteID())
                .noteTitle(note.getNoteTitle())
                .noteDescription(note.getNoteDescription())
                .userID(note.getUserID())
                .build();
    }

}
