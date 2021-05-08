package com.bezkoder.springjwt.security.event;

import org.springframework.context.ApplicationEvent;
import com.bezkoder.springjwt.models.User;

public class OnAnnouncementEvent extends ApplicationEvent {

	private final String content;

	private final User user;

	private final String[] receiptAdresses;

	public OnAnnouncementEvent(final User user, final String[] receiptAdresses, final String content) {
		super(user);
		this.user = user;
		this.content = content;
		this.receiptAdresses = receiptAdresses;
	}

	public String getContent() {
		return content;
	}

	public String[] getReceiptAdresses() {
		return receiptAdresses;
	}

	public User getUser() {
		return user;
	}

}
