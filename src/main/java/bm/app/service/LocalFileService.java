package bm.app.service;

import bm.app.exceptions.ConfigPropertyException;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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


}
