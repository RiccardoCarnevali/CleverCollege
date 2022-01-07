package com.clevercollege.model;

import java.util.List;

public class Seminar extends Activity {

	private String date;

	public Seminar() {
		super();
	}

	public Seminar(long id, String time, int length, String description, User manager, List<Student> bookers,
			Location classroom, String date) {
		super(id, time, length, description, manager, bookers, classroom);
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public boolean checkValid() {
		return (super.checkValid() && date != null);
	}
}
