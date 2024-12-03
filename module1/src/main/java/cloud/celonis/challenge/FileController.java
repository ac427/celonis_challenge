package cloud.celonis.challenge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${app.api.key}")
    private String apiKey;

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @RequestHeader(value = "Celonis-Auth", required = false) String authHeader) {
        if (apiKey.equals(authHeader)) {
            fileStorageService.store(file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{name}")
    public ResponseEntity<FileSystemResource> get(@PathVariable String name) throws IOException {
        Resource resource = fileStorageService.loadAsResource(name);
        if (resource.exists()) {
            return ResponseEntity.ok(new FileSystemResource(resource.getFile()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
