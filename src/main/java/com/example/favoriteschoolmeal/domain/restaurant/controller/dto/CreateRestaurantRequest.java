package com.example.favoriteschoolmeal.domain.restaurant.controller.dto;

import com.example.favoriteschoolmeal.domain.model.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateRestaurantRequest(
        @NotBlank
        String name,
        @NotNull
        Location location,
        @NotNull
        Boolean isOnCampus,
        @NotBlank
        String category,
        @NotBlank
        String businessHours) {
}
