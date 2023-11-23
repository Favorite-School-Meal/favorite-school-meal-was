package com.example.favoriteschoolmeal.domain.restaurant.exeption;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class RestaurantException extends BaseException {

        private final RestaurantExceptionType restaurantExceptionType;

        public RestaurantException(final RestaurantExceptionType restaurantExceptionType) {
            this.restaurantExceptionType = restaurantExceptionType;
        }

        @Override
        public BaseExceptionType exceptionType() {
            return restaurantExceptionType;
        }

}
