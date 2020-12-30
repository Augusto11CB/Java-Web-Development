package aug.bueno.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    private int fileID;
    private String fileName;
    private String contentType;
    private String fileSize;
    private int userID;
    private byte[] fileData;
}
