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
import com.bezkoder.springjwt.security.event.OnAnnouncementEvent;
import com.bezkoder.springjwt.security.event.OnRegistrationEvent;
import com.bezkoder.springjwt.security.event.OnResetPasswordEvent;
import com.bezkoder.springjwt.security.service.IUserService;
import com.bezkoder.springjwt.security.service.UserService;


@Component
public class OnAnnouncementListener implements ApplicationListener<OnAnnouncementEvent> {

    @Autowired
    private IUserService service;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;
    
    @Autowired
    private UserService userService;

    // API

    @Override
    public void onApplicationEvent(final OnAnnouncementEvent event) {
        this.confirmAnnounement(event, event.getReceiptAdresses(), event.getContent());
    }

    private void confirmAnnounement(final OnAnnouncementEvent event, String[] receiptAdresses, String content) {
        final User user = event.getUser();
        
        final SimpleMailMessage email = constructEmailMessage(event, user, receiptAdresses, content );
        mailSender.send(email);
    }

    //

    private SimpleMailMessage constructEmailMessage(final OnAnnouncementEvent event, final User user, String[] receiptAdresses, String content) {
        final String subject = "Announement";
        final SimpleMailMessage email = new SimpleMailMessage(); 
        email.setFrom(user.getEmail());
        email.setTo(receiptAdresses);
        email.setSubject(subject);
        email.setText(content);
        return email;
    }

}
