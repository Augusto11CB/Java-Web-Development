package aug.bueno.c1.l3.chat.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatForm {

    private String userName;
    private String message;
    private String messageType;
}
