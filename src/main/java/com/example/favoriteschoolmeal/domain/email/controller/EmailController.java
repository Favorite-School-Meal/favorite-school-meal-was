package com.example.favoriteschoolmeal.domain.email.controller;


import com.example.favoriteschoolmeal.domain.email.domain.EmailMessage;
import com.example.favoriteschoolmeal.domain.email.dto.EmailPostRequest;
import com.example.favoriteschoolmeal.domain.email.service.EmailService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/email")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> sendTempPassword(@Valid @RequestBody EmailPostRequest request) {

        EmailMessage emailMessage = emailService.setMessage(request);
        emailService.sendEmail(emailMessage);
        return ApiResponse.createSuccess(null);
    }
}
