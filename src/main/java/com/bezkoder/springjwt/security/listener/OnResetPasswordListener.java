package com.bezkoder.springjwt.security.listener;


import java.util.Optional;

import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.PasswordResetTokenRepository;
import com.bezkoder.springjwt.persistence.UserRepository;
import com.bezkoder.springjwt.security.event.OnRegistrationEvent;
import com.bezkoder.springjwt.security.event.OnResetPasswordEvent;
import com.bezkoder.springjwt.security.service.IUserService;
import com.bezkoder.springjwt.security.service.UserService;


@Component
public class OnResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    @Autowired
    private IUserService service;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private UserService userService;

    // API

    @Override
    public void onApplicationEvent(final OnResetPasswordEvent event) {
        this.confirmResetPassword(event);
    }

    private void confirmResetPassword(final OnResetPasswordEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID()
            .toString();
        
        String code = userService.generateCode();
        service.createPasswordResetTokenForUser(user, token, code);

        final SimpleMailMessage email = constructEmailMessage(event, user, token, code);
        mailSender.send(email);
    }

    // 

    private SimpleMailMessage constructEmailMessage(final OnResetPasswordEvent event, final User user, final String token, final String code) {
        final String recipientAddress = user.getEmail();
        final String subject = "Reset Password Confirmation";
        // event.getAppUrl()
//        final String confirmationUrl = "http://localhost:8081" + "/resetPasswordConfirm?token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Your Code : " + code);
        email.setFrom("bonguyens2001@gmail.com");
        return email;
    }

}
