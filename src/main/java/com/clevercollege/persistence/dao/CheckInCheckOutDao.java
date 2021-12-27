package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.CheckInCheckOut;

public interface CheckInCheckOutDao {

	public List<CheckInCheckOut> findAll() throws SQLException;
	
	public CheckInCheckOut findByPrimaryKey(long id) throws SQLException;
	
	public void saveOrUpdate(CheckInCheckOut checkInCheckOut) throws SQLException;
	
	public void delete(long id) throws SQLException;
}
