package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.WeeklyLesson;

public interface WeeklyLessonDao {
	
	public List<WeeklyLesson> findAll() throws SQLException;
	
	public WeeklyLesson findByPrimaryKey(long id) throws SQLException;
	
	public void saveOrUpdate(WeeklyLesson weeklyLesson) throws SQLException;
	
	public void delete(long id) throws SQLException;
	
	public List<WeeklyLesson> findByProfessor(String cf, boolean lazy) throws SQLException;
}
