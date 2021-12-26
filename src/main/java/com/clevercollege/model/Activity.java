package com.clevercollege.model;

import java.sql.Time;
import java.util.List;

public class Activity {

	private long id;
	private Time time;
	private int length;
	private String description;
	private User manager; // must be a professor
	private List<Student> bookers;
	private Location classroom; //must be a classroom

	public Activity() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
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

}
