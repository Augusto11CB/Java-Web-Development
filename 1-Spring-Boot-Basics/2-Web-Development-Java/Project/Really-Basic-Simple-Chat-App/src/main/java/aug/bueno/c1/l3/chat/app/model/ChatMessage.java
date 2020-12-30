package aug.bueno.c1.l3.chat.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String messageid;
    private String username;
    private String messagetext;
    private String messagetype;
}
