package com.example.favoriteschoolmeal.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private String longitude;
    private String latitude;

    public Location() {

    }
}
