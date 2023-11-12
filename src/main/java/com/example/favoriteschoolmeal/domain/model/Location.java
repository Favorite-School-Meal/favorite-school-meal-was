package com.example.favoriteschoolmeal.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Location {
    private String longitude;
    private String latitude;
}
