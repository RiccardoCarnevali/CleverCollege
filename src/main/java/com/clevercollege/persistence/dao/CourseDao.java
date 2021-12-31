package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Course;

public interface CourseDao {

	public List<Course> findAll() throws SQLException;
	
	public List<Course> findByLike(String like, int amount, int offset) throws SQLException;
	
	public List<Course> findByProfessor(String professor) throws SQLException;
	
	public Course findByPrimaryKey(long id) throws SQLException;
	
	public void saveOrUpdate(Course course) throws SQLException;
	
	public void delete(long id) throws SQLException;
}
