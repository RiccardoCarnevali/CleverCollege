package com.clevercollege.model;

import java.util.List;

public class Student extends User {

	private String studentNumber;
	private List<Course> followedCourses;

	public Student() {
		super();
	}

	public Student(String cf, String firstName, String lastName, String email, String password, String description,
			String profilePicture, String studentNumber, List<Course> followedCourses) {
		super(cf, firstName, lastName, email, password, description, profilePicture);
		this.studentNumber = studentNumber;
		this.followedCourses = followedCourses;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	
	public List<Course> getFollowedCourses() {
		return followedCourses;
	}
	
	public void setFollowedCourses(List<Course> followedCourses) {
		this.followedCourses = followedCourses;
	}
}
