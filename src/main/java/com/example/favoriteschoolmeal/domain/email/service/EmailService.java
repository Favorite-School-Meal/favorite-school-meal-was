package com.example.favoriteschoolmeal.domain.email.service;


import com.example.favoriteschoolmeal.domain.email.domain.EmailMessage;
import com.example.favoriteschoolmeal.domain.email.dto.EmailPostRequest;
import com.example.favoriteschoolmeal.domain.email.exception.EmailException;
import com.example.favoriteschoolmeal.domain.email.exception.EmailExceptionType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl.generateRandomString;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender javaMailSender;


    public void sendEmail(EmailMessage emailMessage){


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(emailMessage.getToEmail());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getMessage());
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new EmailException(EmailExceptionType.EMAIL_SEND_FAILURE);
        }

    }

    public EmailMessage setMessage(EmailPostRequest request){

        String tempPassword = createTempPassword();
        String username = request.username();

        return EmailMessage.builder()
                .toEmail(request.email())
                .subject("[최애의 학식] 임시 비밀번호")
                .message(username + "님의 임시 비밀번호 입니다.\r\n" + tempPassword)
                .build();
    }

    private  String createTempPassword(){
        return generateRandomString(10);
    }
}
