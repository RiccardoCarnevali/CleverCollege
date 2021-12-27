package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Lesson;

public interface LessonDao {

	public List<Lesson> findAll(boolean lazy) throws SQLException;
	
	public Lesson findByPrimaryKey(long id, boolean lazy) throws SQLException;
	
	public void saveOrUpdate(Lesson lesson) throws SQLException;
	
	public void delete(long id) throws SQLException;
	
}
