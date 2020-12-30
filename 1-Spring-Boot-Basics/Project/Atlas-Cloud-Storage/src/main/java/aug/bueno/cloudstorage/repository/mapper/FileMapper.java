package aug.bueno.cloudstorage.repository.mapper;

import aug.bueno.cloudstorage.model.File;
import aug.bueno.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userID}")
    List<File> findAllFilesByUser(int userID);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileID}  AND userid = #{userID}")
    Optional<File> getFileByIdAndUser(int fileID, int userID);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userID}")
    List<File> findFileByNameAndUserID(String fileName, int userID);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userID}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileID")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileID}")
    int delete(Integer fileID);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileID} AND userid= #{userID}")
    int deleteByFileIDAndUserID(Integer fileID, Integer userID);

    @Delete("DELETE FROM FILES")
    void deleteAll();
}
