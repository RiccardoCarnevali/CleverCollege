package com.clevercollege.model;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.persistence.DatabaseManager;

public class LessonProxy extends Lesson {

	public LessonProxy() {
		super();
	}
	
	public LessonProxy(long id, String time, int length, String description, User manager, Location classroom, Course course) {
		super(id, time, length, description, manager, null, classroom, course);
	}

	@Override
	public List<Student> getBookers() {
		if(super.getBookers() == null)
			try {
				setBookers(DatabaseManager.getInstance().getStudentDao().findBookersForActivity(getId()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return super.getBookers();
	}
	
}
