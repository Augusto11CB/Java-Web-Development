package aug.bueno.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    private Integer credentialID;
    private String url;
    private String userName;
    private String key;
    private String password;
    private int userID;

}
