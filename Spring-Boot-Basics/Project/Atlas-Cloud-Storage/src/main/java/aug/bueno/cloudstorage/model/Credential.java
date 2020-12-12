package aug.bueno.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    private int credentialID;
    private String url;
    private String userName;
    private String key;
    private String password;

    private User user;
//    private int userID;

}
