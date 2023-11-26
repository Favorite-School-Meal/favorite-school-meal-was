package com.example.favoriteschoolmeal.domain.file.controller;

import com.example.favoriteschoolmeal.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/images/{savedName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String savedName) {

        String savedPath = fileDir + savedName;
        Resource resource;
        try {
            resource = new UrlResource(new File(savedPath).toURI());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error loading image: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(resource);
    }

    /**
     * 테스트용 업로드
     * */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(MultipartFile files) {
        return ResponseEntity.ok(fileService.saveFile(files).toString());
    }
}
