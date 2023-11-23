package com.example.favoriteschoolmeal.domain.restaurant.service;

import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.CreateRestaurantRequest;
import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.RestaurantResponse;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import com.example.favoriteschoolmeal.domain.restaurant.exeption.RestaurantExceptionType;
import com.example.favoriteschoolmeal.domain.restaurant.exeption.RestaurantNotFoundException;
import com.example.favoriteschoolmeal.domain.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantResponse addRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.name())
                .location(request.location())
                .isOnCampus(request.isOnCampus())
                .category(request.category())
                .businessHours(request.businessHours())
                .thumbnailUrl(request.thumbnailUrl())
                .menuImageUrl(request.menuImageUrl())
                .build();

        restaurantRepository.save(restaurant);
        return RestaurantResponse.of(restaurant);
    }

    // TODO: PR에서 리팩토링 제안 예정 (주석 작성자: 조예림)
    public RestaurantResponse findRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        RestaurantExceptionType.RESTAURANT_NOT_FOUND));

        return RestaurantResponse.of(restaurant);

    }

    public List<RestaurantResponse> findAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream().map(RestaurantResponse::of).toList();
    }

    public RestaurantResponse modifyRestaurant(Long restaurantId, CreateRestaurantRequest request,
            MultipartFile thumbnail, MultipartFile menuImage) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        RestaurantExceptionType.RESTAURANT_NOT_FOUND));

        restaurant.update(request.isOnCampus(), request.location(), request.category(),
                request.name(), request.businessHours(), request.thumbnailUrl(), request.menuImageUrl());
        return RestaurantResponse.of(restaurant);
    }


    public Long deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        RestaurantExceptionType.RESTAURANT_NOT_FOUND));

        restaurantRepository.delete(restaurant);
        return restaurantId;
    }

    public Optional<Restaurant> findRestaurantOptionally(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }
}
