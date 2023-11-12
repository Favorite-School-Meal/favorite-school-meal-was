package com.example.favoriteschoolmeal.domain.restaurant.service;

import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.CreateRestaurantRequest;
import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.RestaurantResponse;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import com.example.favoriteschoolmeal.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantResponse addRestaurant(CreateRestaurantRequest request, MultipartFile thumbnail, MultipartFile menuImage) {
        //TODO: file처리 추가

        Restaurant restaurant = Restaurant.builder()
                .name(request.name())
                .location(request.location())
                .isOnCampus(request.isOnCampus())
                .category(request.category())
                .businessHours(request.businessHours())
                //TODO: thumbnail, menuImage 처리 추가
                .build();

        restaurantRepository.save(restaurant);
        return RestaurantResponse.of(restaurant);
    }
}
