package aug.bueno.cloudstorage.repository.mapper;

import aug.bueno.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userID}")
    List<Note> getAllNotesByUser(int userID);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteID}  AND userid = #{userID}")
    Note getNoteByIdAndUser(int noteID, int userID);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle} AND userid = #{userID}")
    List<Note> getNoteByTitleAndUserID(String noteTitle, int userID);
    // TODO - Verify if we can have notes with same title

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userID})")
    @Options(useGeneratedKeys = true, keyProperty = "noteID")
    int insert(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteID}")
    void delete(Integer noteID);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteID} AND userid= #{userID}")
    void deleteByNoteIDAndUserID(Integer noteID, Integer userID);

    @Delete("DELETE FROM NOTES")
    void deleteAll();

    @Update("UPDATE notes " +
            "SET notetitle = #{noteTitle}, notedescription = #{noteDescription} " +
            "WHERE noteid = #{noteID}")
    int update(Note note);
}

