package com.clevercollege.model;

import java.util.Objects;

public class CheckInCheckOut {

	private long id;
	private String inTime;
	private String outTime;
	private String date;
	private User user;
	private Location location;

	public CheckInCheckOut() {

	}

	public CheckInCheckOut(long id, String inTime, String outTime, String date, User user, Location location) {
		super();
		this.id = id;
		this.inTime = inTime;
		this.outTime = outTime;
		this.date = date;
		this.user = user;
		this.location = location;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckInCheckOut other = (CheckInCheckOut) obj;
		return id == other.id;
	}

}
