package com.clevercollege.model;

import java.util.List;

public class Lesson extends Activity {
	
	private Course course;
	
	public Lesson() {
		super();
	}
	
	public Lesson(long id, String time, int length, String description, User manager, List<Student> bookers,
			Location classroom, Course course) {
		super(id, time, length, description, manager, bookers, classroom);
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
	@Override
	public boolean checkValid() {
		return (super.checkValid() && course != null);
	}
}
