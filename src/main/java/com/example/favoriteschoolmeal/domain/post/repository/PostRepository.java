package com.example.favoriteschoolmeal.domain.post.repository;

import com.example.favoriteschoolmeal.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.matching m ORDER BY m.matchingState DESC, p.createdAt DESC")
    Page<Post> findAllOrderByStatusAndTime(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.matching m WHERE p.restaurant.id = :restaurantId ORDER BY m.matchingState DESC, p.createdAt DESC")
    Page<Post> findAllByRestaurantIdOrderByStatusAndTime(Long restaurantId, Pageable pageable);
}
