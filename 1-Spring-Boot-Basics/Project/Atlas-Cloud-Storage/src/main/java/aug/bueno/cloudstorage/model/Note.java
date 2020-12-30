package aug.bueno.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    private int noteID;
    private String noteTitle;
    private String noteDescription;
    private int userID;

}
