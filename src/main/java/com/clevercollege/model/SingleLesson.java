package com.clevercollege.model;

import java.util.List;

public class SingleLesson extends Lesson {

	private String date;

	public SingleLesson() {
		super();
	}

	public SingleLesson(long id, String time, int length, String description, User manager, List<Student> bookers,
			Location classroom, Course course, String date) {
		super(id, time, length, description, manager, bookers, classroom, course);
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public boolean checkValid() {
		return(super.checkValid() && date != null);
	}
}
