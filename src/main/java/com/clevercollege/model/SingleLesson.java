package com.clevercollege.model;

import java.sql.Date;

public class SingleLesson extends Lesson {

	private Date date;

	public SingleLesson() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
