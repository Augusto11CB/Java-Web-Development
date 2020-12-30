package aug.bueno.cloudstorage.services;

import aug.bueno.cloudstorage.dto.FileFormDTO;
import aug.bueno.cloudstorage.model.File;
import aug.bueno.cloudstorage.repository.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<FileFormDTO> findAllUserFiles(final int userID) {
        return fileMapper.findAllFilesByUser(userID).stream()
                .map(this::fileToFileFormDTO)
                .collect(Collectors.toList());
    }

    public Optional<FileFormDTO> findFileByFileIDAndUserID(final int fileID, final int userID) {

        Optional<File> fileByIdAndUser = fileMapper.getFileByIdAndUser(fileID, userID);

        if (fileByIdAndUser.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.fileToFileFormDTO(fileByIdAndUser.get()));
    }

    public List<FileFormDTO> findFileByFileNameAndUserID(final String fileName, final int userID) {
        return fileMapper.findFileByNameAndUserID(fileName, userID).stream()
                .map(this::fileToFileFormDTO)
                .collect(Collectors.toList());
    }

    public boolean insertFile(final FileFormDTO fileFormDTO, final int userID) {

        final File file = File.builder()
                .contentType(fileFormDTO.getContentType())
                .fileName(fileFormDTO.getFileName())
                .fileSize(fileFormDTO.getFileSize())
                .fileData(fileFormDTO.getFileData())
                .userID(userID)
                .build();

        return fileMapper.insert(file) > 0;

    }

    public int deleteByNoteIDAndUserID(final int fileID, final int userID) {
        return fileMapper.deleteByFileIDAndUserID(fileID, userID);
    }

    public int deleteByFileID(final int fileID) {
        return fileMapper.delete(fileID);
    }

    private FileFormDTO fileToFileFormDTO(final File file) {
        return FileFormDTO.builder()
                .fileID(file.getFileID())
                .contentType(file.getContentType())
                .fileName(file.getFileName())
                .fileSize(file.getFileSize())
                .fileData(file.getFileData())
                .userID(file.getUserID())
                .build();
    }
}

