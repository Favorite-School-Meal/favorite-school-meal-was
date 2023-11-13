package com.example.favoriteschoolmeal.domain.restaurant.controller;

import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.CreateRestaurantRequest;
import com.example.favoriteschoolmeal.domain.restaurant.controller.dto.RestaurantResponse;
import com.example.favoriteschoolmeal.domain.restaurant.service.RestaurantService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/{restaurantId}")
    public ApiResponse<RestaurantResponse> restaurantDetails(@PathVariable("restaurantId") Long restaurantId){
        RestaurantResponse response = restaurantService.findRestaurant(restaurantId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping
    public ApiResponse<List<RestaurantResponse>> restaurantList(){
        List<RestaurantResponse> response = restaurantService.findAllRestaurant();
        return ApiResponse.createSuccess(response);
    }

    @PutMapping("/{restaurantId}")
    public ApiResponse<RestaurantResponse> restaurantModify(@PathVariable("restaurantId") Long restaurantId,
                                                            @RequestPart("dto") CreateRestaurantRequest request,
                                                            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                                            @RequestPart(value = "menuImage", required = false) MultipartFile menuImage){
        RestaurantResponse response = restaurantService.modifyRestaurant(restaurantId, request, thumbnail, menuImage);
        return ApiResponse.createSuccess(response);
    }


}
