package com.clevercollege.model;

public class Student extends User {

	private String studentNumber;

	public Student() {
		super();
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

}
