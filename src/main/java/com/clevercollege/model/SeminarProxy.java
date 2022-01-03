package com.clevercollege.model;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import com.clevercollege.persistence.DatabaseManager;

public class SeminarProxy extends Seminar {

	public SeminarProxy() {
		super();
	}
	
	public SeminarProxy(long id, Time time, int length, String description, User manager, Location classroom, Date date) {
		super(id, time, length, description, manager, null, classroom, date);
	}

	@Override
	public List<Student> getBookers() {
		if(super.getBookers() == null)
			try {
				setBookers(DatabaseManager.getInstance().getStudentDao().findBookersForActivity(getId(), true));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return super.getBookers();
	}
	
}
