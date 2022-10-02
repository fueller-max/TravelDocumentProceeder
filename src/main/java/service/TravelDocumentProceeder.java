package service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import service.DocumentProceederService.DocumentProceedService;
import service.storageService.StorageException;
import service.storageService.StorageFileNotFoundException;
import service.storageService.StorageService;


import java.io.IOException;


@Controller
public class TravelDocumentProceeder {

    private final StorageService storageService;
    private final DocumentProceedService documentProceedService;

    @Autowired
    public TravelDocumentProceeder(StorageService storageService,
                                   DocumentProceedService documentProceedService ) {
        this.storageService = storageService;
        this.documentProceedService = documentProceedService;
    }

    @GetMapping("/")
    public String getUploadForm(Model model) throws IOException {
        return "uploadForm";
    }

    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) throws IOException {

        // Upload the file to the directory
        Resource fileUploaded = storageService.loadAsResource(fileName);

        //Process the data and return path to the file generated
        Resource fileModified = documentProceedService.proceed(fileUploaded.getURL().getFile());

        //Return the generated file
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileModified.getFilename() + "\"").body(fileModified);

    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                file.getOriginalFilename());
        return "redirect:/";
    }

    @ExceptionHandler({StorageFileNotFoundException.class})
    public ResponseEntity<?> handleFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({StorageException.class})
    public ResponseEntity<?> StorageException(StorageException exc) {
        return ResponseEntity.notFound().build();

    }
}
