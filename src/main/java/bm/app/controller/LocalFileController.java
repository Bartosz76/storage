package bm.app.controller;

import bm.app.model.LocalFile;
import bm.app.service.LocalFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class LocalFileController {

    private LocalFileService localFileService;

    public LocalFileController(LocalFileService localFileService){
        this.localFileService = localFileService;
    }

    @GetMapping("/files")
    public List<LocalFile> getFiles(){
        return localFileService.getFiles();
    }

    @GetMapping("/files/download/{filename}")
    public ResponseEntity<?> getFile(@PathVariable String filename){
        return localFileService.getFile(filename);
    }

    @GetMapping("/files/delete//{filename}")
    public ResponseEntity<?> deleteFile(@PathVariable String filename){
        return localFileService.deleteFile(filename);
    }
}
