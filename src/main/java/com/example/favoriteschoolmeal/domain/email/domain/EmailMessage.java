package com.example.favoriteschoolmeal.domain.email.domain;

import lombok.Builder;

@Builder
public class EmailMessage {

    private String toEmail;
    private String subject;
    private String message;
}
