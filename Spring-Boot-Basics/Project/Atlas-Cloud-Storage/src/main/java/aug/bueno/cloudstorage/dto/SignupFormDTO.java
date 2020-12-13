package aug.bueno.cloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupFormDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
}
