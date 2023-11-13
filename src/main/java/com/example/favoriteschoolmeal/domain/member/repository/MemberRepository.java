package com.example.favoriteschoolmeal.domain.member.repository;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
}
