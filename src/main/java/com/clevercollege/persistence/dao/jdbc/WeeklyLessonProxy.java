package com.clevercollege.persistence.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Student;
import com.clevercollege.model.WeeklyLesson;
import com.clevercollege.persistence.DatabaseManager;

public class WeeklyLessonProxy extends WeeklyLesson {

	public WeeklyLessonProxy() {
		super();
	}

	@Override
	public List<Student> getBookers() {
		if (super.getBookers() == null)
			try {
				setBookers(DatabaseManager.getInstance().getStudentDao().findBookersForActivity(getId(), true));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return super.getBookers();
	}

}
