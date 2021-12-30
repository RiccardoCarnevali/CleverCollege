package com.clevercollege.persistence.dao;

import java.sql.SQLException;

import com.clevercollege.model.RecoveryToken;

public interface RecoveryTokenDao {
	
	public RecoveryToken findByPrimaryKey(String cf) throws SQLException;
	
	public void saveOrUpdate(RecoveryToken token) throws SQLException;
	
	public void delete(String cf) throws SQLException;
	
}
