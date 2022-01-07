package com.clevercollege.model;

import java.util.List;
import java.util.Objects;

public class Activity {

	private long id;
	private String time;
	private int length;
	private String description;
	private User manager; // must be a professor
	private List<Student> bookers;
	private Location classroom; // must be a classroom

	public Activity() {

	}

	public Activity(long id, String time, int length, String description, User manager, List<Student> bookers,
			Location classroom) {
		super();
		this.id = id;
		this.time = time;
		this.length = length;
		this.description = description;
		this.manager = manager;
		this.bookers = bookers;
		this.classroom = classroom;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public List<Student> getBookers() {
		return bookers;
	}

	public void setBookers(List<Student> bookers) {
		this.bookers = bookers;
	}

	public Location getClassroom() {
		return classroom;
	}

	public void setClassroom(Location classroom) {
		this.classroom = classroom;
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
		Activity other = (Activity) obj;
		return id == other.id;
	}

	public boolean checkValid() {
		if(time != null && description != null && manager != null && classroom != null)
			return true;
		return false;
	}

}
