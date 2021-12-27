package com.clevercollege.model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class SingleLesson extends Lesson {

	private Date date;

	public SingleLesson() {
		super();
	}

	public SingleLesson(long id, Time time, int length, String description, User manager, List<Student> bookers,
			Location classroom, Course course, Date date) {
		super(id, time, length, description, manager, bookers, classroom, course);
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
