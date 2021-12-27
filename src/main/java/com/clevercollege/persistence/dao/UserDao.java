package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.User;

public interface UserDao {

	public List<User> findAll() throws SQLException;
	
	public User findByPrimaryKey(String cf) throws SQLException;
	
	public void saveOrUpdate(User user) throws SQLException;
	
	public void delete(String cf) throws SQLException;
	
}
