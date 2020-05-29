package bm.app.service;

import bm.app.exceptions.ConfigPropertyException;
import bm.app.model.LocalFile;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static bm.app.config.Constans.DELETE_URI;
import static bm.app.config.Constans.DOWNLOAD_URI;

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

        Path path = Paths.get(this.uploads);
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
        Path path = Paths.get(this.uploads + fileName);
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
            logger.info("Try to get all files");
            files = Files.walk(Paths.get(this.uploads)).filter(Files::isRegularFile);
        }catch (IOException e){
            logger.error("Cannot get files: {}", e.getMessage());
            return null;
        }
        List<LocalFile> localFiles = new ArrayList();
        files.forEach(
                f -> {
                    BasicFileAttributes bs;
                    try {
                        bs = Files.readAttributes(f, BasicFileAttributes.class);
                    }catch (IOException e){
                        logger.error("Cannot get the file: {}", e.getMessage());
                        return;
                    }
                    String downloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path(DOWNLOAD_URI)
                            .path(f.getFileName().toString())
                            .toUriString();

                    String deleteUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path(DELETE_URI)
                            .path(f.getFileName().toString())
                            .toUriString();



                    LocalFile localFile = new LocalFile();
                    localFile.setName(f.getFileName().toString());
                    localFile.setCreationTime(bs.creationTime().toString());
                    localFile.setLastModified(bs.lastModifiedTime().toString());
                    localFile.setSize(bs.size());
                    localFile.setDownloadURI(downloadUri);
                    localFile.setDeleteURI(deleteUri);

                    try {
                        localFile.setFileType(Files.probeContentType(f.toAbsolutePath()));
                    }catch (IOException e){
                        logger.error("Error while getting probeContentType: {}", e.getMessage());
                    }

                    localFiles.add(localFile);

                }

        );
        return localFiles;

    }

    public ResponseEntity<String> deleteFile(String filename){
        File file = new File(this.uploads + filename);

        if (file.delete()){
            logger.info("Deleted the file: {}" + file.getName());
            return new ResponseEntity<>("Deleted the file." + file.getName(), HttpStatus.OK);
        }
        logger.error("File not found: {}" + file.getName());
        return new ResponseEntity<>("File not found." + file.getName(), HttpStatus.NOT_FOUND);
    }
}
