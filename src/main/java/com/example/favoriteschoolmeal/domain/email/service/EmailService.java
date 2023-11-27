package com.example.favoriteschoolmeal.domain.email.service;

import com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl;
import com.example.favoriteschoolmeal.domain.email.domain.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl.generateRandomString;

@Service
@RequiredArgsConstructor
public class EmailService {

    private JavaMailSender javaMailSender;


    private static void sendEmail(EmailMessage emailMessage){

    }

    private  String createTemporaryPassword(){
        return generateRandomString(10);
    }
}
