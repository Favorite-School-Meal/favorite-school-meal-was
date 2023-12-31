package com.example.favoriteschoolmeal.domain.member.domain;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.member.dto.BlockRequest;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.Gender;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Base {

    // 기본 프로필 이미지 엔드포인트
    private final transient String DEFAULT_PROFILE_IMAGE_ENDPOINT = "/images/default_profile_image.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true, updatable = false)
    private String username;

    @Column(name = "nickname", unique = true, nullable = false, length = 50)
    private String nickname;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, length = 50, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "unblock_date", nullable = true)
    private LocalDateTime unblockDate;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "introduction", length = 300)
    private String introduction;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity profileImage;

    @Builder
    public Member(String username, String nickname, String fullName, String password, String email,
                  Authority authority, LocalDateTime unblockDate, Integer age, Gender gender,
                  String introduction) {
        this.username = username;
        this.nickname = nickname;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.authority = authority;
        this.unblockDate = unblockDate;
        this.age = age;
        this.gender = gender;
        this.introduction = introduction;
    }


    public void block(BlockRequest blockRequest) {
        Long blockHours = blockRequest.blockHours();
        if (this.unblockDate == null || this.unblockDate.isBefore(LocalDateTime.now())) {
            this.unblockDate = LocalDateTime.now().plusHours(blockHours);
        } else {
            this.unblockDate = this.unblockDate.plusHours(blockHours);
        }
    }

    public void changeProfileImage(FileEntity fileEntity) {
        this.profileImage = fileEntity;
    }

    public boolean isBanned() {
        return this.unblockDate != null && this.unblockDate.isAfter(LocalDateTime.now());
    }

    public void modifyNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void modifyIntroduction(final String introduction) {
        this.introduction = introduction;
    }


    public void summarizeIntroduction(String summarizedIntroduction) {
        this.introduction = summarizedIntroduction;
    }

    public void unblock() {
        this.unblockDate = null;
    }

    public void modifyPassword(final String password) {
        this.password = password;
    }

    public String getProfileImageEndpoint() {
        Optional<FileEntity> profileImage = Optional.ofNullable(this.profileImage);
        return profileImage.map(FileEntity::getEndpoint).orElse(DEFAULT_PROFILE_IMAGE_ENDPOINT);
    }

}