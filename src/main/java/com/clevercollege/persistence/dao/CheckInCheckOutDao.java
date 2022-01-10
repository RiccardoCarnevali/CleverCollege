package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.CheckInCheckOut;
import com.clevercollege.model.Location;
import com.clevercollege.model.Student;

public interface CheckInCheckOutDao {

	public List<CheckInCheckOut> findAll() throws SQLException;
	
	public CheckInCheckOut findByPrimaryKey(long id) throws SQLException;
	
	public void saveOrUpdate(CheckInCheckOut checkInCheckOut) throws SQLException;
	
	public void delete(long id) throws SQLException;

	public List<CheckInCheckOut> findByUser(String cf) throws SQLException;

	public CheckInCheckOut findActiveByUser(String cf) throws SQLException;

	public Location findPlaceOfCheckIn(String cfUser) throws SQLException;

	public List<Student> findCheckInStudentsByLocation(String nameLocation)throws SQLException;
}
