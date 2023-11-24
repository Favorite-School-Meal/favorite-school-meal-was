package com.example.favoriteschoolmeal.domain.restaurant.controller;

import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.CreateRestaurantRequest;
import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.RestaurantResponse;
import com.example.favoriteschoolmeal.domain.restaurant.service.RestaurantService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ApiResponse<RestaurantResponse> restaurantAdd(@RequestBody CreateRestaurantRequest request) {
        RestaurantResponse response = restaurantService.addRestaurant(request);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/{restaurantId}")
    public ApiResponse<RestaurantResponse> restaurantDetails(@PathVariable("restaurantId") Long restaurantId) {
        RestaurantResponse response = restaurantService.findRestaurant(restaurantId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping
    public ApiResponse<List<RestaurantResponse>> restaurantList() {
        List<RestaurantResponse> response = restaurantService.findAllRestaurant();
        return ApiResponse.createSuccess(response);
    }

    @PutMapping("/{restaurantId}")
    public ApiResponse<RestaurantResponse> restaurantModify(@PathVariable("restaurantId") Long restaurantId,
                                                            @RequestBody CreateRestaurantRequest request) {
        RestaurantResponse response = restaurantService.modifyRestaurant(restaurantId, request);
        return ApiResponse.createSuccess(response);
    }

    @DeleteMapping("/{restaurantId}")
    public ApiResponse<Long> restaurantDelete(@PathVariable("restaurantId") Long restaurantId) {
        Long deletedRestaurantId = restaurantService.deleteRestaurant(restaurantId);
        return ApiResponse.createSuccess(deletedRestaurantId);
    }

}
