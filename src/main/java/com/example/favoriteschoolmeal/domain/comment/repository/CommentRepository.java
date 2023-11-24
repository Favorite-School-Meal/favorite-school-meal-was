package com.example.favoriteschoolmeal.domain.comment.repository;

import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post, Sort sort);

    /**
     * 주어진 게시물에 대한 댓글 수를 반환합니다.
     *
     * @param post 게시물 엔티티
     * @return 해당 게시물의 댓글 수
     */
    int countByPost(Post post);
}
