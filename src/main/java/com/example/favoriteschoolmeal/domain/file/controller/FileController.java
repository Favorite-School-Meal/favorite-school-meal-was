package com.example.favoriteschoolmeal.domain.file.controller;

import com.example.favoriteschoolmeal.domain.file.exeption.FileException;
import com.example.favoriteschoolmeal.domain.file.exeption.FileExceptionType;
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

     /**
      * /images/{savedName} 형식으로 요청하면 해당 이미지를 불러옵니다.
      *
      * @param savedName 불러올 이미지의 이름
      *                  (이미지의 이름은 uuid + 확장자로 구성되며, FileEntity의 savedName 필드 값과 동일합니다.)
      *                  ex) 1234-1234-1234-1234.jpg
      *
      * @return 이미지 파일
     **/
    @GetMapping("/images/{savedName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String savedName) {

        Resource resource = fileService.loadFileAsResource(savedName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(resource);
    }
}
