package aug.bueno.cloudstorage.repository.mapper;

import aug.bueno.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE userid = #{userid}")
    Optional<User> getUserById(int userid);

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    Optional<User> getUserByName(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) " +
            "VALUES(#{userName}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userID")
    int insert(User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userid}")
    void deleteUser(int userid);
}
