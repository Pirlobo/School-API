package com.bezkoder.springjwt.security.listener;


import java.util.UUID;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.security.event.OnRegistrationEvent;
import com.bezkoder.springjwt.security.service.IUserService;


@Component
public class OnRegistrationListener implements ApplicationListener<OnRegistrationEvent> {

    @Autowired
    private IUserService service;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    // API

    @Override
    public void onApplicationEvent(final OnRegistrationEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID()
            .toString();
        service.createVerificationTokenForUser(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    //

    private SimpleMailMessage constructEmailMessage(final OnRegistrationEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/api/auth/registrationConfirm?token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Please open the following URL to verify your account: \r\n" + confirmationUrl);
        email.setFrom("bonguyens2001@gmail.com");
        return email;
    }

}
