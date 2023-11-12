package com.example.favoriteschoolmeal.domain.restaurant.repository;

import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
