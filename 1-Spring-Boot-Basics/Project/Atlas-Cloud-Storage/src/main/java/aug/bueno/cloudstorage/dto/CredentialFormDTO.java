package aug.bueno.cloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialFormDTO {

    private Integer credentialID;
    private String url;
    private String userName;
    private String key;
    private String password;
    private int userID;
}
