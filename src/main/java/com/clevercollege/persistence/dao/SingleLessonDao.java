package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.SingleLesson;

public interface SingleLessonDao {
	
	public List<SingleLesson> findAll(boolean lazy) throws SQLException;

	public SingleLesson findByPrimaryKey(long id, boolean lazy) throws SQLException;

	public void saveOrUpdate(SingleLesson singleLesson) throws SQLException;

	public void delete(long id) throws SQLException;

	public List<SingleLesson> findByProfessor(String cf, boolean lazy) throws SQLException;
}
