package com.example.favoriteschoolmeal.domain.post.repository;

import com.example.favoriteschoolmeal.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
