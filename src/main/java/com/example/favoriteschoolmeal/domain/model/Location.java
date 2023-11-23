package com.example.favoriteschoolmeal.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Location {
    private Double longitude;
    private Double latitude;

    public Location() {

    }
}
