package com.example.favoriteschoolmeal.domain.restaurant.service;

import com.example.favoriteschoolmeal.domain.restaurant.repository.RestaurantRepository;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantDTO.Response restaurantAdd(RestaurantDTO.Request request, MultipartFile thumbnail, MultipartFile menuImage){
        //TODO: file처리 추가

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .location(request.getLocation())
                .isOnCampus(request.getIsOnCampus())
                .category(request.getCategory())
                .businessHours(request.getBusinessHours())
                //TODO: thumbnail, menuImage 처리 추가
                .build();

        restaurantRepository.save(restaurant);
        return RestaurantDTO.Response.of(restaurant);
    }
}
