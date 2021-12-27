package com.clevercollege.model;

import java.sql.Time;
import java.util.Objects;

public class CheckInCheckOut {

	private long id;
	private Time inTime;
	private Time outTime;
	private User user;
	private Location location;

	public CheckInCheckOut() {

	}

	public CheckInCheckOut(long id, Time inTime, Time outTime, User user, Location location) {
		super();
		this.id = id;
		this.inTime = inTime;
		this.outTime = outTime;
		this.user = user;
		this.location = location;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Time getInTime() {
		return inTime;
	}

	public void setInTime(Time inTime) {
		this.inTime = inTime;
	}

	public Time getOutTime() {
		return outTime;
	}

	public void setOutTime(Time outTime) {
		this.outTime = outTime;
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
