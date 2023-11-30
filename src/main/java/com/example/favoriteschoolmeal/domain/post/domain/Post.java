package com.example.favoriteschoolmeal.domain.post.domain;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id")
    private Matching matching;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @Builder
    public Post(final Member member, final Matching matching, final Restaurant restaurant,
            final String title, final String content) {
        this.member = member;
        this.matching = matching;
        this.restaurant = restaurant;
        this.title = title;
        this.content = content;
    }

    public void modifyTitleAndContent(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public void summarizeContent(String summarizedContent) {
        this.content = summarizedContent;
    }
}
