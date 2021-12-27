package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Location;

public interface ClassroomDao {

	public List<Location> findAll() throws SQLException;

	public Location findByPrimaryKey(long id) throws SQLException;

	public void saveOrUpdate(Location classroom) throws SQLException;

	public void delete(long id) throws SQLException;
}
