package cloud.celonis.challenge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class LocalFileFileStorageService implements FileStorageService {

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    @Override
    public void store(MultipartFile file) {
        try {
            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename());
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        return new FileSystemResource(uploadDir + File.separator + StringUtils.cleanPath(filename));
    }

}
