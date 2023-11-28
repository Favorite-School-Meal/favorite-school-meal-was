package com.example.favoriteschoolmeal.domain.email.service;


import com.example.favoriteschoolmeal.domain.email.domain.EmailMessage;
import com.example.favoriteschoolmeal.domain.email.dto.EmailPostRequest;
import com.example.favoriteschoolmeal.domain.email.exception.EmailException;
import com.example.favoriteschoolmeal.domain.email.exception.EmailExceptionType;
import com.example.favoriteschoolmeal.domain.member.domain.Member;

import com.example.favoriteschoolmeal.domain.member.dto.ModifyPasswordRequest;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;

import com.example.favoriteschoolmeal.domain.member.service.MemberService;
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
    private MemberRepository memberRepository;
    private MemberService memberService;

    public void sendEmail(final EmailMessage emailMessage){

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

    public EmailMessage setMessage(final EmailPostRequest request){

        Member member = getMemberOrThrow(request);

        String tempPassword = createTempPassword();
        String username = request.username();

        ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest(tempPassword);
        modifyPasswordToTempPassword(member, modifyPasswordRequest);

        return EmailMessage.builder()
                .toEmail(request.email())
                .subject("[최애의 학식] 임시 비밀번호")
                .message(username + "님의 임시 비밀번호 입니다.\r\n" + tempPassword)
                .build();
    }

    private void modifyPasswordToTempPassword(final Member member, final ModifyPasswordRequest request){
        memberService.modifyMemberPassword(member, request);
    }

    private String createTempPassword(){
        return generateRandomString(10);
    }

    private Member getMemberOrThrow(final EmailPostRequest request){
        return memberRepository.findByUsernameAndEmail(request.username(), request.email())
                .orElseThrow(() -> new EmailException(EmailExceptionType.MEMBER_NOT_FOUND));
    }


}
