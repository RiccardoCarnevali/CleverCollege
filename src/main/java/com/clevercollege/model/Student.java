package com.clevercollege.model;

public class Student extends User {

	private String studentNumber;

	public Student() {
		super();
	}

	public Student(String cf, String firstName, String lastName, String email, String password, String description,
			String profilePicture, String studentNumber) {
		super(cf, firstName, lastName, email, password, description, profilePicture);
		this.studentNumber = studentNumber;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
}
