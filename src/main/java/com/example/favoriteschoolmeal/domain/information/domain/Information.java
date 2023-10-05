package com.example.favoriteschoolmeal.domain.information.domain;

import com.example.favoriteschoolmeal.domain.file.domain.File;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.Library;
import com.example.favoriteschoolmeal.domain.model.MealType;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "information")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Information extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "information_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    @Column(name = "library", nullable = false)
    private Library library;

    @Column(name = "image_id", nullable = false, length = 100)
    private Long ImageId;

    @Builder
    public Information(Member member, MealType mealType, Library library, Long ImageId) {
        this.member = member;
        this.mealType = mealType;
        this.library = library;
        this.ImageId = ImageId;
    }
}
