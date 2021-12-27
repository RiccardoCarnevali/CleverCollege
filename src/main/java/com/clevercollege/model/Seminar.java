package com.clevercollege.model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Seminar extends Activity {

	private Date date;

	public Seminar() {
		super();
	}

	public Seminar(long id, Time time, int length, String description, User manager, List<Student> bookers,
			Location classroom, Date date) {
		super(id, time, length, description, manager, bookers, classroom);
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
