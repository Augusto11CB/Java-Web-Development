package aug.bueno.c1.l3.chat.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int userid;
    private String username;
    private String salt;
    private String password;
    private String firstname;
    private String lastname;
}
