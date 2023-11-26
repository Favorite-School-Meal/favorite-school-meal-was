package com.example.favoriteschoolmeal.domain.file.service;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    public Long saveFile(MultipartFile files){
        if (files.isEmpty()) {
            return null;
        }

        // 원래 파일 이름 추출
        String origName = files.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 파일 엔티티 생성
        FileEntity file = FileEntity.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build();

        // 실제로 로컬에 uuid를 파일명으로 저장
        try {
            files.transferTo(new File(savedPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 데이터베이스에 파일 정보 저장
        FileEntity savedFile = fileRepository.save(file);

        return savedFile.getId();
    }

    public FileEntity findFileOrThrow(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));
    }
}
