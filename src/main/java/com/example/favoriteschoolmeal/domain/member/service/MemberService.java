package com.example.favoriteschoolmeal.domain.member.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.dto.*;
import com.example.favoriteschoolmeal.domain.member.exception.MemberException;
import com.example.favoriteschoolmeal.domain.member.exception.MemberExceptionType;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public Optional<Member> findMemberOptionally(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public void blockMember(Member reportedMember, Long blockHours) {
        reportedMember.block(blockHours);
    }


    public MemberDetailResponse modifyMember(final ModifyMemberRequest request, final Long memberId){

        verifyUserOrAdmin();
        final Long currentMemberId = getCurrentMemberId();

        final Member member = getMemberOrThrow(memberId);
        verifyMemberOwner(memberId, currentMemberId);

        modifyIntroduction(member, request);
        final Member savedMember = memberRepository.save(member);

        return MemberDetailResponse.from(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberDetailResponse findDetailMember(final Long memberId){

        final Member member = getMemberOrThrow(memberId);
        return MemberDetailResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberSimpleResponse findSimpleMember(final Long memberId){
        final Member member = getMemberOrThrow(memberId);
        return MemberSimpleResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberDetailResponse findCurrentMember(){

        final Long currentMemberId = getCurrentMemberId();
        final Member member = getMemberOrThrow(currentMemberId);

        return MemberDetailResponse.from(member);
    }

    @Transactional(readOnly = true)
    public PaginatedMemberListResponse findAllMember(final Pageable pageable){

        verifyAdmin();

        final Page<Member> members = memberRepository.findAllOrderByCreatedAt(pageable);
        summarizeMembersIfNotNull(members);

        List<MemberSummaryResponse> responses = members.stream()
                .map(this::convertToSummaryResponse).toList();

        return PaginatedMemberListResponse.from(responses, members.getNumber(),
                members.getTotalPages(), members.getTotalElements());
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

    private void verifyAdmin(){
        SecurityUtils.checkAdminOrThrow(
                () -> new MemberException(MemberExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyMemberOwner(final Long memberOwnerId, final Long currentMemberId){
        if(!memberOwnerId.equals(currentMemberId)){
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_ACCESS);
        }
    }

    private Long getCurrentMemberId(){
        return SecurityUtils.getCurrentMemberId(
                () -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    private MemberSummaryResponse convertToSummaryResponse(final Member member){
        return MemberSummaryResponse.from(member);
    }

    private void summarizeIntroduction(final Member member){
        String summarizedIntroduction = Optional.ofNullable(member.getIntroduction())
                .filter(introduction -> introduction.length() > 50)
                .map(introduction -> introduction.substring(0, 50) + "...")
                .orElse(member.getIntroduction());

        member.summarizeIntroduction(summarizedIntroduction);
    }

    private void summarizeMembersIfNotNull(Page<Member> members){
        if(!members.isEmpty()){
            members.forEach(this::summarizeIntroduction);
        }
    }


}
