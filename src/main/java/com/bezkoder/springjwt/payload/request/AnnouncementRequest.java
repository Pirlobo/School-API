package com.bezkoder.springjwt.payload.request;

import com.sun.istack.NotNull;

public class AnnouncementRequest {
	@NotNull
	private String userName;

	private String content;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
