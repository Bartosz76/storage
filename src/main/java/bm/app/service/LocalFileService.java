package bm.app.service;

import bm.app.exceptions.ConfigPropertyException;
import bm.app.model.LocalFile;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LocalFileService {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileService.class);

    //@Value("${files.path}")
    private String uploads = "C:\\Users\\User\\Desktop\\Ascension\\appFiles\\storageFiles\\";

    public LocalFileService() {
        try {
            createContextDirectory();
        } catch (ConfigPropertyException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createContextDirectory() throws ConfigPropertyException, IOException {
        if (Strings.isBlank(this.uploads)) {
            logger.error("Cannot get the path to files");
            throw new ConfigPropertyException("Cannot get the path to files");
        }

        Path path = Paths.get(uploads);
        if (Files.notExists(path)) {
            try {
                logger.info("Try to create a directory: {}", path);
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("Cannot create the directory: {}. Exception: {}", path, e.getMessage());
                throw new IOException(e.getMessage());
            }
        }
    }

    public ResponseEntity<?> getFile(String fileName){
        Resource resource;
        Path path = Paths.get(uploads);
        try {
            resource = new UrlResource(path.toUri());
        }catch (MalformedURLException e){
            logger.error("Cannot get the file: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        File targetFile;
        try {
            targetFile = resource.getFile();
        }catch (IOException e){
            e.printStackTrace();
            logger.error("Cannot get the file: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        String contentType;
        try {
            contentType = Files.probeContentType(path);
        }catch (IOException e){
            logger.error("Cannot get the file: {}", e.getMessage());
            return ResponseEntity
                    .ok()
                    .body(e.getMessage());
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachement;filename=\"" + targetFile.getName() + "\"")
                .contentLength(targetFile.length())
                .body(resource);
    }


    public List<LocalFile> getFiles() {
        Stream<Path> files;
        try{
            Files.walk(Paths.get(uploads))
                    .filter()
        }

    }
}
