package com.example.favoriteschoolmeal.domain.file.service;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.file.exeption.FileException;
import com.example.favoriteschoolmeal.domain.file.exeption.FileExceptionType;
import com.example.favoriteschoolmeal.domain.file.repository.FileRepository;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    // 이미지를 불러올 때 사용할 경로
    private final String viewPath = "/api/v1/images/";
    @Value("${file.dir}")
    private String fileDir;

    /**
     * 파일을 저장하고 저장된 파일의 id를 반환하는 메서드입니다.
     *
     * @param files 저장할 파일
     * @return 저장된 파일의 id
     */
    public Long saveFile(MultipartFile files) {
        if (files.isEmpty()) {
            return null;
        }
        String origName = getOriginalNameOrThrow(files);
        String savedName = getSavedNameOrThrow(origName);
        String savedPath = fileDir + savedName;

        transferFileToSavedPath(files, savedPath);

        FileEntity file = createFileEntity(origName, savedName, savedPath);
        FileEntity savedFile = fileRepository.save(file);

        return savedFile.getId();
    }

    public Optional<FileEntity> findFileOptionally(Long id) {
        return fileRepository.findById(id);
    }

    public Resource loadFileAsResource(String savedName) {
        String savedPath = fileDir + savedName;
        Resource resource;
        try {
            resource = new UrlResource(new File(savedPath).toURI());
        } catch (MalformedURLException e) {
            throw new FileException(FileExceptionType.MALFORMED_URL);
        }
        return resource;
    }



    public void removeFileEntityByMember(final Long fileId) {
        findFileOptionally(fileId).ifPresent(fileRepository::delete);
    }


    private FileEntity createFileEntity(String origName, String savedName, String savedPath) {
        return FileEntity.builder()
                .originalName(origName)
                .savedName(savedName)
                .savedPath(savedPath)
                .endpoint(viewPath + savedName)
                .build();
    }


    /**
     * 실제로 로컬에 파일을 저장하는 메서드입니다.
     */
    private void transferFileToSavedPath(MultipartFile files, String savedPath) {
        try {
            files.transferTo(new File(savedPath));
        } catch (IOException e) {
            throw new FileException(FileExceptionType.FILE_IO_EXCEPTION);
        }
    }

    private String getSavedNameOrThrow(String origName) {
        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        String extension = getExtensionOrThrow(origName);

        // uuid와 확장자 결합
        return uuid + extension;
    }

    private String getExtensionOrThrow(String origName) {
        // 확장자 추출(ex : .jpg) 현재 jpg만 허용
        String extension = origName.substring(origName.lastIndexOf("."));
        if (!extension.equalsIgnoreCase(".jpg")) {
            throw new FileException(FileExceptionType.UNSUPPORTED_EXTENSION);
        }
        return extension;
    }

    private String getOriginalNameOrThrow(MultipartFile files) {
        // 원래 파일 이름 추출
        String origName = files.getOriginalFilename();
        if (origName == null || origName.isEmpty()) {
            throw new FileException(FileExceptionType.EMPTY_FILE_NAME);
        }
        return origName;
    }



}
