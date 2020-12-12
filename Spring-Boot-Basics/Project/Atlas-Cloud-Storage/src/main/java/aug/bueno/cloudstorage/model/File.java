package aug.bueno.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    private int fileID;
    private String fileName;
    private String contentType;
    private String fileSize;

    private User user;
//    private int userID;

    // TODO - Verify how to map BLOB data into object field
    private byte[] fileData;
}
