package com.clevercollege.model;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.persistence.DatabaseManager;

public class StudentProxy extends Student {

	public StudentProxy() {
		super();
	}
	
	public StudentProxy(String cf, String firstName, String lastName, String email, String password, String description,
			String profilePicture, String studentNumber) {
		super(cf, firstName, lastName, email, password, description, profilePicture, studentNumber, null);
	}
	
	@Override
	public List<Course> getFollowedCourses() {
		if(super.getFollowedCourses() == null) {
			try {
				setFollowedCourses(DatabaseManager.getInstance().getCourseDao().findCoursesFollowedBy(getCf()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return super.getFollowedCourses();
	}
}
