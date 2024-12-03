package cloud.celonis.challenge;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void store(MultipartFile file);

    Resource loadAsResource(String filename);
}
