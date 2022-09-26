package service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import service.storage.StorageFileNotFoundException;
import service.storage.StorageService;


import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService ){
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        storageService.loadAll().forEach(System.out::println);
       model.addAttribute("files",storageService.loadAll().map(
               path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,"serveFile",
                                                              path.getFileName().toString()).build().toUri().toString())
                       .collect(Collectors.toList()));
    return "uploadForm";
    }

    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName){
        Resource file = storageService.loadAsResource(fileName);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes){
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " +
                                             file.getOriginalFilename() + "!");
        return "redirect:/";
    }

    @ExceptionHandler({StorageFileNotFoundException.class})
    public ResponseEntity<?> handleFileNotFound(StorageFileNotFoundException exc)
    {
        return ResponseEntity.notFound().build();
    }


}