package com.example.favoriteschoolmeal.domain.restaurant.controller.dto;

import com.example.favoriteschoolmeal.domain.model.Location;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;

public record RestaurantResponse(
        Long id,
        String name,
        Location location,
        Boolean isOnCampus,
        String category,
        String businessHours,
        String thumbnail_url,
        String menuImage_url
) {
    public static RestaurantResponse of(final Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getLocation(),
                restaurant.getIsOnCampus(),
                restaurant.getCategory(),
                restaurant.getBusinessHours(),
                //TODO: thumbnail_url, menuImage_url 처리 추가
                "thumbnail_url",
                "menuImage_url"
        );
    }
}
