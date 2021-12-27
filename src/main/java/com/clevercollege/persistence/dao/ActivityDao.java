package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Activity;

public interface ActivityDao {

	public List<Activity> findAll(boolean lazy) throws SQLException;
	
	public Activity findByPrimaryKey(long id, boolean lazy) throws SQLException;
	
	public void saveOrUpdate(Activity activity) throws SQLException;
	
	public void delete(long id) throws SQLException;
	
}
