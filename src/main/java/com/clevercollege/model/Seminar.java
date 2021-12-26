package com.clevercollege.model;

import java.sql.Date;

public class Seminar extends Activity {

	private Date date;

	public Seminar() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
