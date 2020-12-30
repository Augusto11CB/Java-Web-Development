package aug.bueno.cloudstorage.repository.mapper;

import aug.bueno.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userID}")
    List<Credential> getAllCredentialByUser(int userID);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialID}  AND userid = #{userID}")
    Credential getCredentialByIdAndUser(int credentialID, int userID);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES(#{url}, #{userName}, #{key}, #{password}, #{userID})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialID")
    int insert(Credential Credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialID}")
    boolean delete(Integer credentialID);

    // TODO DELETE FUNCTIONS RETURNING int

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialID} AND userid= #{userID}")
    void deleteByCredentialIDAndUserID(Integer credentialID, Integer userID);

    @Delete("DELETE FROM CREDENTIALS")
    void deleteAll();

    @Update("UPDATE CREDENTIALS " +
            "SET url = #{url}, username = #{userName},  key = #{key},  password = #{password},  userid = #{userID} " +
            "WHERE credentialid = #{credentialID}")
    int update(Credential Credential);

}
