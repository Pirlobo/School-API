package com.bezkoder.springjwt.models;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "building")
public class Building {

	@Id
	private Integer id;
	
	private String buildingName;
	
	@OneToMany(mappedBy = "building" )
	private List<Room> rooms = new ArrayList<Room>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public Building(Integer id, String buildingName) {
		super();
		this.id = id;
		this.buildingName = buildingName;
		
	}

	public Building() {
		super();
	}
	
	
}
