package com.example.favoriteschoolmeal.domain.email.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailMessage {

    private String toEmail;
    private String subject;
    private String message;
}
