package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.User;

public interface AdministratorDao {

	public List<User> findAll() throws SQLException;
	
	public List<User> findByLike(String sortBy, String like, int amount, int offset) throws SQLException;

	public User findByPrimaryKey(String cf) throws SQLException;

	public void saveOrUpdate(User admin) throws SQLException;

	public void delete(String cf) throws SQLException;
}
