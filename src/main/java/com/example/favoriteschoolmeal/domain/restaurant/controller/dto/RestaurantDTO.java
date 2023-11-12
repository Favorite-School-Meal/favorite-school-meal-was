package com.example.favoriteschoolmeal.domain.restaurant.controller.dto;

import com.example.favoriteschoolmeal.domain.model.Location;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class RestaurantDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request{
        private String name;
        private Location location;
        private Boolean isOnCampus;
        private String category;
        private String businessHours;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String name;
        private Location location;
        private Boolean isOnCampus;
        private String category;
        private String businessHours;
        private String thumbnail_url;
        private String menuImage_url;

        public static Response of(Restaurant restaurant) {
            return Response.builder()
                    .name(restaurant.getName())
                    .location(restaurant.getLocation())
                    .isOnCampus(restaurant.getIsOnCampus())
                    .category(restaurant.getCategory())
                    .businessHours(restaurant.getBusinessHours())
                    //TODO: thumbnail_url, menuImage_url 처리 추가
                    .build();
        }
    }
}
