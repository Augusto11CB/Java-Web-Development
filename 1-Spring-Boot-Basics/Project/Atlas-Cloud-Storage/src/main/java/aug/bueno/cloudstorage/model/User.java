package aug.bueno.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private int userID;
    private String userName;
    private String salt;
    private String password;
    private String firstName;
    private String lastName;
}
