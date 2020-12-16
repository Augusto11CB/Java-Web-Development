package aug.bueno.cloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteFormDTO {

    private Integer noteID;
    private String noteTitle;
    private String noteDescription;
    private Integer userID;
}
