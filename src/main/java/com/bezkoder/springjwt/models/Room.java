package com.bezkoder.springjwt.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "room")
public class Room {

	@Id
	private Integer id;
	
	
	public Room() {
		super();
	}

	private String roomName;
	
	@ManyToOne
	private Building building;

	
	
	@OneToMany(mappedBy = "course")
	private List<Classroom> calendars = new ArrayList<Classroom>();
	
	
	
	
	

	

	public List<Classroom> getCalendars() {
		return calendars;
	}

	public void setCalendars(List<Classroom> calendars) {
		this.calendars = calendars;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Room(Integer id, String roomName, Building building) {
		super();
		this.id = id;
		this.roomName = roomName;
		this.building = building;
		
	}

	
	
	
	
}

