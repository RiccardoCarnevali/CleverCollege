package com.clevercollege.model;

public class Lesson extends Activity {
	
	private Course course;
	
	public Lesson() {
		super();
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
}
