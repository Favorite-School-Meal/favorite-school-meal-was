package com.example.favoriteschoolmeal.domain.member.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.dto.MemberDetailResponse;
import com.example.favoriteschoolmeal.domain.member.dto.ModifyMemberRequest;
import com.example.favoriteschoolmeal.domain.member.dto.PaginatedMemberListResponse;
import com.example.favoriteschoolmeal.domain.member.exception.MemberException;
import com.example.favoriteschoolmeal.domain.member.exception.MemberExceptionType;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void blockMember(Member reportedMember, Long blockHours) {
        reportedMember.block(blockHours);
    }


    public MemberDetailResponse modifyMember(final ModifyMemberRequest request, final Long memberId){

        verifyUserOrAdmin();
        final Member member = getMemberOrThrow(memberId);

        modifyIntroduction(member, request);
        final Member savedMember = memberRepository.save(member);

        return MemberDetailResponse.from(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberDetailResponse findMember(final Long memberId){

        final Member member = getMemberOrThrow(memberId);
        return MemberDetailResponse.from(member);
    }


    @Transactional(readOnly = true)
    public PaginatedMemberListResponse findAllPost(final Pageable pageable){

        return null;
    }

    private void modifyIntroduction(final Member member, final ModifyMemberRequest request){
        member.modifyIntroduction(request.introduction());
    }

    private Member getMemberOrThrow(final Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }


    private void verifyUserOrAdmin(){
        SecurityUtils.checkUserOrAdminOrThrow(
                () -> new MemberException(MemberExceptionType.UNAUTHORIZED_ACCESS));
    }

    private Long getCurrentMemberId(){
        return SecurityUtils.getCurrentMemberId(
                () -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }


}
