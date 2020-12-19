package aug.bueno.cloudstorage.controller;

import aug.bueno.cloudstorage.dto.FileFormDTO;
import aug.bueno.cloudstorage.services.FileService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static aug.bueno.cloudstorage.controller.util.MessageWrapperUtil.*;
/*
 * Files
 * - [X] Upload: On successful file upload, the user should be shown a success message and the uploaded file should appear in the list.
 *
 * - [X] Deletion: On successful file deletion, the user should be shown a success message and the deleted file should disappear from the list.
 *
 * - [X] Download: On successful file download, the file should download to the user's system.
 *
 * - [X] Errors: Users should be notified of errors if they occur.
 */

@Controller
@RequestMapping("/file")
public class FileController {

    private Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload")
    public String insertNewFile(@RequestParam("fileUpload") MultipartFile file, ModelMap modelMap) {

        int userID = 1;
        Optional<String> invalidFileToSave = isInvalidFileToSave(file, userID);

        if (invalidFileToSave.isPresent()) {
            return invalidFileToSave.get();
        }

        boolean result = false;
        try {
            FileFormDTO build = FileFormDTO.builder()
                    .fileData(file.getBytes())
                    .fileName(file.getOriginalFilename())
                    .fileSize(String.valueOf(file.getSize()))
                    .contentType(file.getContentType())
                    .build();

            result = fileService.insertFile(build, userID);

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return result ? "redirect:/result?isSuccess=" + true : "redirect:/result?error=" + true;
    }

    @GetMapping("/download/{fileID}")
    public String downloadFile(@PathVariable Integer fileID, RedirectAttributes redirectAttributes,
                               HttpServletResponse resp) throws IOException {

        int userID = 1;

        Optional<FileFormDTO> fileByFileNameAndUserID = fileService.findFileByFileIDAndUserID(fileID, userID);

        if (fileByFileNameAndUserID.isPresent()) {
            FileFormDTO fileFormDTO = fileByFileNameAndUserID.get();
            resp.setContentType("application/octet-stream");
            resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileFormDTO.getFileName() + "\"");
            InputStream inputStream = new ByteArrayInputStream(fileFormDTO.getFileData());
            IOUtils.copy(inputStream, resp.getOutputStream());
            resp.flushBuffer();

            return "/home";
        }
        return "redirect:/result?errorMessage=" + FILE_ALREADY_EXIST_MSG;
    }

    /*
        // TODO Aks Udacity Knowledge Center about this case
        @GetMapping("/download/{fileID}")
        public ResponseEntity<?> viewFile(@PathVariable Integer fileID, HttpServletResponse response) throws IOException {
            int userID = 1;
            Optional<FileFormDTO> fileByFileNameAndUserID = Optional.empty();

            if (fileByFileNameAndUserID.isPresent()) {
                FileFormDTO fileFormDTO = fileByFileNameAndUserID.get();
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileFormDTO.getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileFormDTO.getFileName() + "\"")
                        .body(new ByteArrayResource(fileFormDTO.getFileData()));
            } else {
                response.sendRedirect("result?errorMessage=" + FILE_ALREADY_EXIST_MSG);
                return ResponseEntity.notFound().build();
            }

            return fileByFileNameAndUserID.map(fileFormDTO -> {
                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(fileFormDTO.getContentType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileFormDTO.getFileName() + "\"")
                                .body(new ByteArrayResource(fileFormDTO.getFileData()));
                    }
            ).orElseGet(() -> {
                        throw new FileNotFoundException("FileID" + fileID + "not found");
                    }
            );

            return fileByFileNameAndUserID.map(fileFormDTO -> {
                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(fileFormDTO.getContentType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileFormDTO.getFileName() + "\"")
                                .body(new ByteArrayResource(fileFormDTO.getFileData()));
                    }
            ).orElseGet(() -> {
                        return ResponseEntity
                                .notFound()
                                .header("Location", "/result?errorMessage=" + FILE_ALREADY_EXIST_MSG)
                                .build();
                    }
            );
        }

        public @ResponseBody
            byte[] getFile(@PathVariable("fileID") Integer fileID) {
            int userID = 1;
            Optional<FileFormDTO> fileByFileNameAndUserID = fileService.findFileByFileIDAndUserID(fileID, userID);
            return fileByFileNameAndUserID.get().getFileData();
        }
    */


    @GetMapping("/delete/{fileID}")
    public String deleteFile(
            ModelMap model,
            @PathVariable("fileID") Integer fileID
    ) {
        LOGGER.info(fileID.toString());
        int userID = 1;

        int result = 0;
        try {

            if (fileID > 0) {
                result = fileService.deleteByFileID(fileID);
            }

            return result == 1 ? "redirect:/result?isSuccess=" + true : "redirect:/result?error=" + true;

        } catch (Exception e) {
            this.LOGGER.error(e.getMessage());
            return "redirect:/result?error=" + false;
        }
    }

    private Optional<String> isInvalidFileToSave(MultipartFile file, int userID) {

        if (file.getSize() == 0) {
            return Optional.of("redirect:/result?errorMessage=" + EMPTY_FILE_MSG);
        }

        if (Objects.isNull(file.getOriginalFilename()) || file.getOriginalFilename().equals("")) {
            return Optional.of("redirect:/result?errorMessage=" + INVALID_FILE_MSG);
        }

        List<FileFormDTO> fileByFileNameAndUserID = fileService.findFileByFileNameAndUserID(file.getOriginalFilename(), userID);

        if (!fileByFileNameAndUserID.isEmpty()) {

            return Optional.of("redirect:/result?errorMessage=" + FILE_ALREADY_EXIST_MSG);
        }

        return Optional.empty();
    }
}
