package com.clevercollege.persistence;

import java.sql.SQLException;

public interface IdBroker {

	public long getNextActivityId() throws SQLException;
	
	public long getNextCheckInCheckOutId() throws SQLException;
	
	public long getNextCourseId() throws SQLException;
	
	public long getNextLocationId() throws SQLException;
	
}
