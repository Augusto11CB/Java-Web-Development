package aug.bueno.c1.l3.chat.app.repository.mapper;

import aug.bueno.c1.l3.chat.app.model.ChatMessage;
import aug.bueno.c1.l3.chat.app.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    @Select("SELECT * FROM MESSAGES")
    List<ChatMessage> getAllChatMessages();

    @Select("SELECT * FROM MESSAGES WHERE messageid = #{messageid}")
    ChatMessage getChatMessageById(int messageid);

    @Select("SELECT * FROM MESSAGES WHERE username = #{username}")
    List<ChatMessage> getChatMessagesByName(String username);

    @Insert("INSERT INTO MESSAGES (username, messagetext, messagetype) VALUES(#{username}, #{messagetext}, #{messagetype})")
    @Options(useGeneratedKeys = true, keyProperty = "messageid")
    int insert(ChatMessage user);

}
