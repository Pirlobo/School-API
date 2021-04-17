package com.bezkoder.springjwt.security.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.security.event.OnAnnouncementEvent;
import com.bezkoder.springjwt.security.event.OnRegistrationEvent;

@Component
public class OnAnnouncementListener implements ApplicationListener<OnAnnouncementEvent> {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(final OnAnnouncementEvent event) {
		this.confirmAnnounement(event, event.getReceiptAdresses(), event.getContent());
	}

	private void confirmAnnounement(final OnAnnouncementEvent event, String[] receiptAdresses, String content) {
		final User user = event.getUser();
		final SimpleMailMessage email = constructEmailMessage(event, user, receiptAdresses, content);
		mailSender.send(email);
	}

	// Send any important information to all students
	private SimpleMailMessage constructEmailMessage(final OnAnnouncementEvent event, final User user,
			String[] receiptAdresses, String content) {
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(user.getEmail());
		email.setTo(receiptAdresses);
		email.setSubject("Announement");
		email.setText(content);
		return email;
	}

}
