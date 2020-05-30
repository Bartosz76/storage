package bm.app.controller;

import bm.app.model.LocalFile;
import bm.app.service.LocalFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1")
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

    @DeleteMapping("/files/delete//{filename}")
    public ResponseEntity<?> deleteFile(@PathVariable String filename){
        return localFileService.deleteFile(filename);
    }

    @PostMapping("/files")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        return localFileService.uploadFile(file);
    }
}
