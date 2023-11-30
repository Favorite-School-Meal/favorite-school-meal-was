package com.example.favoriteschoolmeal.domain.post.repository;

import com.example.favoriteschoolmeal.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.matching m ORDER BY m.matchingStatus DESC, p.createdAt DESC")
    Page<Post> findAllOrderByStatusAndTime(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.matching m WHERE p.restaurant.id = :restaurantId ORDER BY m.matchingStatus DESC, p.createdAt DESC")
    Page<Post> findAllByRestaurantIdOrderByStatusAndTime(Pageable pageable,
            @Param("restaurantId") Long restaurantId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.matching m WHERE p.member.id = :memberId ORDER BY m.matchingStatus DESC, p.createdAt DESC")
    Page<Post> findAllByMemberIdOrderByStatusAndTime(Pageable pageable,
            @Param("memberId") Long memberId);

    List<Post> findAllByMemberId(Long memberId);
}
