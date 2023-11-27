package com.example.favoriteschoolmeal.domain.file.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Entity
@Table(name = "file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String originalName;

    private String savedName;

    private String savedPath;

    @Builder
    public FileEntity(String originalName, String savedName, String savedPath) {
        this.originalName = originalName;
        this.savedName = savedName;
        this.savedPath = savedPath;
    }
}