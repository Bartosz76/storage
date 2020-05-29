package bm.app.controller;

import bm.app.model.LocalFile;
import bm.app.service.LocalFileService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
