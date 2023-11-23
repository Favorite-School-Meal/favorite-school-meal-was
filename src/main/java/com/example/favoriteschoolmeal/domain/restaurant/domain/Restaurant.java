package com.example.favoriteschoolmeal.domain.restaurant.domain;

import com.example.favoriteschoolmeal.domain.model.Location;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id", updatable = false)
    private Long id;

    @Column(name = "is_on_campus", nullable = false)
    private Boolean isOnCampus;

    @Embedded
    private Location location;

    @Column(name = "category", nullable = false, length = 100)
    private String category; //업종을 문자열로 저장

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "business_hours", nullable = false, length = 100)
    private String businessHours;

    @Column(name = "thumbnail_url", nullable = false, length = 100)
    private String thumbnailUrl;

    @Column(name = "menu_image_url", nullable = true, length = 100)
    private String menuImageUrl;

    @Builder
    public Restaurant(Boolean isOnCampus, Location location, String category, String name, String businessHours, String thumbnailUrl, String menuImageUrl) {
        this.isOnCampus = isOnCampus;
        this.location = location;
        this.category = category;
        this.name = name;
        this.businessHours = businessHours;
        this.thumbnailUrl = thumbnailUrl;
        this.menuImageUrl = menuImageUrl;
    }

    public void update(Boolean isOnCampus, Location location, String category, String name, String businessHours, String thumbnailUrl, String menuImageUrl) {
        this.isOnCampus = isOnCampus;
        this.location = location;
        this.category = category;
        this.name = name;
        this.businessHours = businessHours;
        this.thumbnailUrl = thumbnailUrl;
        this.menuImageUrl = menuImageUrl;
    }
}
