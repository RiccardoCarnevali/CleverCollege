package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.User;

public interface ProfessorDao {

	public List<User> findAll() throws SQLException;
	
	public User findByPrimaryKey(String cf) throws SQLException;
	
	public void saveOrUpdate(User professor) throws SQLException;
	
	public void delete(String cf) throws SQLException;

	public List<User> professorWithSubstring(String substring) throws SQLException;
}
