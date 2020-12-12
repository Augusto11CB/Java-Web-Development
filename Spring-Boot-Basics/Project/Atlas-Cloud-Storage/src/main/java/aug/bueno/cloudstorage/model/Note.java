package aug.bueno.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    private int noteID;
    private String noteTitle;
    private String noteDescription;

    // TODO - Verify how relationship between objects work in MyBatis
    private User user;
//    private int userID;

}
