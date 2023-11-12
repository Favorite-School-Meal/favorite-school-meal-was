package com.example.favoriteschoolmeal.domain.restaurant.controller;

import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.CreateRestaurantRequest;
import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.RestaurantResponse;
import com.example.favoriteschoolmeal.domain.restaurant.service.RestaurantService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ApiResponse<RestaurantResponse> restaurantAdd(@RequestPart("dto") CreateRestaurantRequest request,
                                                         @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                                         @RequestPart(value = "menuImage", required = false) MultipartFile menuImage){
        RestaurantResponse response = restaurantService.addRestaurant(request, thumbnail, menuImage);
        return ApiResponse.createSuccess(response);
    }



}
