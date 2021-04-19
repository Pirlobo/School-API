package com.bezkoder.springjwt.payload.response;

import java.util.Date;

public class AssignmentResponse {
	private String name;
	private String url;
	private String type;
	private long size;
	private String description;
	private Integer points;
	private Date date;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public AssignmentResponse(String name, String url, String type, long size, String description, Integer points,
			Date date) {
		super();
		this.name = name;
		this.url = url;
		this.type = type;
		this.size = size;
		this.description = description;
		this.points = points;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
