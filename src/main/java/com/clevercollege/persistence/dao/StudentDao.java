package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Student;

public interface StudentDao {

	public List<Student> findAll() throws SQLException;
	
	public Student findByPrimaryKey(String cf) throws SQLException;
	
	public void saveOrUpdate(Student student) throws SQLException;
	
	public void delete(String cf) throws SQLException;
	
	public List<Student> findBookersForActivity(long activityId) throws SQLException;
}
