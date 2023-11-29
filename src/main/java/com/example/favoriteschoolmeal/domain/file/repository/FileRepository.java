package com.example.favoriteschoolmeal.domain.file.repository;


import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
