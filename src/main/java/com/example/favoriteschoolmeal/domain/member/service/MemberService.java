package com.example.favoriteschoolmeal.domain.member.service;


import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> findMemberOptionally(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
