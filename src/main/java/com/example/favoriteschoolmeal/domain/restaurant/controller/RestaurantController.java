package com.example.favoriteschoolmeal.domain.restaurant.controller;

import com.example.favoriteschoolmeal.domain.restaurant.service.RestaurantService;
import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.RestaurantDTO;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ApiResponse<RestaurantDTO.Response> restaurantAdd(@RequestPart("dto") RestaurantDTO.Request request,
                                                             @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                                             @RequestPart(value = "menuImage", required = false) MultipartFile menuImage){
        RestaurantDTO.Response response = restaurantService.restaurantAdd(request, thumbnail, menuImage);
        return ApiResponse.createSuccess(response);
    }



}
