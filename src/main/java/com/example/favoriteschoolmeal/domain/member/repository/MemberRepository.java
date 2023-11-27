package com.example.favoriteschoolmeal.domain.member.repository;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m ORDER BY m.createdAt DESC")
    Page<Member> findAllOrderByCreatedAt(Pageable pageable);

    Optional<Member> findByFullNameAndEmail(String fullname, String email);
}
