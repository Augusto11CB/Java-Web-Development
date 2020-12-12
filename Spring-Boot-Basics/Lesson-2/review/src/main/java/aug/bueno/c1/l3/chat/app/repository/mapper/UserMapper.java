package aug.bueno.c1.l3.chat.app.repository.mapper;

import aug.bueno.c1.l3.chat.app.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE userid = #{userid}")
    User getUserById(int userid);

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUserByName(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) " +
            "VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userid}")
    void deleteUser(int userid);
}
