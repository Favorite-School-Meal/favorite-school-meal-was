package com.example.favoriteschoolmeal.domain.member.service;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.file.service.FileService;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.dto.FindUsernameRequest;
import com.example.favoriteschoolmeal.domain.member.dto.MemberDetailResponse;
import com.example.favoriteschoolmeal.domain.member.dto.MemberSimpleResponse;
import com.example.favoriteschoolmeal.domain.member.dto.MemberSummaryResponse;
import com.example.favoriteschoolmeal.domain.member.dto.ModifyMemberRequest;
import com.example.favoriteschoolmeal.domain.member.dto.ModifyPasswordRequest;
import com.example.favoriteschoolmeal.domain.member.dto.PaginatedMemberListResponse;
import com.example.favoriteschoolmeal.domain.member.exception.MemberException;
import com.example.favoriteschoolmeal.domain.member.exception.MemberExceptionType;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;


    public Optional<Member> findMemberOptionally(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public void blockMember(Member reportedMember, Long blockHours) {
        reportedMember.block(blockHours);
    }


    public MemberDetailResponse modifyMember(final ModifyMemberRequest request,
            final Long memberId) {

        final Long currentMemberId = getCurrentMemberId();

        final Member member = getMemberOrThrow(memberId);
        verifyMemberOwner(memberId, currentMemberId);

        if (!member.getNickname().equals(request.nickname())) {
            modifyNickname(member, request);
        }

        modifyIntroduction(member, request);

        final Member savedMember = memberRepository.save(member);

        return MemberDetailResponse.from(savedMember);
    }

    public MemberDetailResponse saveProfileImage(Long memberId, MultipartFile file) {
        verifyUserOrAdmin();
        final Long currentMemberId = getCurrentMemberId();

        final Member member = getMemberOrThrow(memberId);
        verifyMemberOwner(memberId, currentMemberId);

        final Long fileId = fileService.saveFile(file);
        FileEntity fileEntity = getFileEntityOrThrow(fileId);
        member.changeProfileImage(fileEntity);

        return MemberDetailResponse.from(member);
    }


    public MemberDetailResponse modifyMemberPassword(final ModifyPasswordRequest request,
            final Long memberId) {

        final Long currentMemberId = getCurrentMemberId();

        final Member member = getMemberOrThrow(memberId);
        verifyMemberOwner(memberId, currentMemberId);

        modifyPassword(member, request);

        final Member savedMember = memberRepository.save(member);

        return MemberDetailResponse.from(savedMember);
    }

    //EmailServive의 호출을 위해 생성
    public void modifyMemberPassword(final Member member, final ModifyPasswordRequest request) {
        modifyPassword(member, request);

    }

    @Transactional(readOnly = true)
    public MemberDetailResponse findDetailMember(final Long memberId) {

        final Member member = getMemberOrThrow(memberId);
        return MemberDetailResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberSimpleResponse findSimpleMember(final Long memberId) {
        final Member member = getMemberOrThrow(memberId);
        return MemberSimpleResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberDetailResponse findCurrentMember() {

        final Long currentMemberId = getCurrentMemberId();
        final Member member = getMemberOrThrow(currentMemberId);

        return MemberDetailResponse.from(member);
    }

    @Transactional(readOnly = true)
    public PaginatedMemberListResponse findAllMember(final Pageable pageable) {

        verifyAdmin();

        final Page<Member> members = memberRepository.findAllOrderByCreatedAt(pageable);
        summarizeMembersIfNotNull(members);

        List<MemberSummaryResponse> responses = members.stream()
                .map(this::convertToSummaryResponse).toList();

        return PaginatedMemberListResponse.from(responses, members.getNumber(),
                members.getTotalPages(), members.getTotalElements());
    }


    @Transactional(readOnly = true)
    public MemberSimpleResponse findUsername(final FindUsernameRequest request) {

        final Member member = getMemberOrThrow(request);
        return MemberSimpleResponse.from(member);
    }


    private void modifyNickname(final Member member, final ModifyMemberRequest request) {
        checkNicknameDuplication(request);
        member.modifyNickname(request.nickname());
    }

    private void modifyIntroduction(final Member member, final ModifyMemberRequest request) {
        member.modifyIntroduction(request.introduction());
    }

    private void modifyPassword(final Member member, final ModifyPasswordRequest request) {
        member.modifyPassword(passwordEncoder.encode(request.password()));
    }

    private Member getMemberOrThrow(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    private Member getMemberOrThrow(final FindUsernameRequest request) {
        return memberRepository.findByFullNameAndEmail(request.fullname(), request.email())
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    private void checkNicknameDuplication(final ModifyMemberRequest request) {
        if (memberRepository.findByNickname(request.nickname()).isPresent()) {
            throw new MemberException(MemberExceptionType.DUPLICATE_NICKNAME_EXCEPTION);
        }
    }

    private void verifyUserOrAdmin() {
        SecurityUtils.checkUserOrAdminOrThrow(
                () -> new MemberException(MemberExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyAdmin() {
        SecurityUtils.checkAdminOrThrow(
                () -> new MemberException(MemberExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyMemberOwner(final Long memberOwnerId, final Long currentMemberId) {
        if (!memberOwnerId.equals(currentMemberId)) {
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_ACCESS);
        }
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    private MemberSummaryResponse convertToSummaryResponse(final Member member) {
        return MemberSummaryResponse.from(member);
    }

    private void summarizeIntroduction(final Member member) {
        String summarizedIntroduction = Optional.ofNullable(member.getIntroduction())
                .filter(introduction -> introduction.length() > 50)
                .map(introduction -> introduction.substring(0, 50) + "...")
                .orElse(member.getIntroduction());

        member.summarizeIntroduction(summarizedIntroduction);
    }

    private void summarizeMembersIfNotNull(Page<Member> members) {
        if (!members.isEmpty()) {
            members.forEach(this::summarizeIntroduction);
        }
    }


    public PaginatedMemberListResponse getPaginatedMemberListResponse(Page<Member> members) {
        summarizeMembersIfNotNull(members);
        List<MemberSummaryResponse> list = members.stream().map(this::convertToSummaryResponse)
                .toList();
        return PaginatedMemberListResponse.from(list, members.getNumber(), members.getTotalPages(),
                members.getTotalElements());
    }

    private FileEntity getFileEntityOrThrow(Long fileId) {
        return fileService.findFileOptionally(fileId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.FILE_NOT_FOUND));
    }


}
