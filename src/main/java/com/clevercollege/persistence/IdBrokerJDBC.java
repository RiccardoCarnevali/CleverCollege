package com.clevercollege.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IdBrokerJDBC implements IdBroker {
	
	private Connection conn;
	
	public IdBrokerJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long getNextActivityId() throws SQLException {
		
		String query = "select nextval('activity_ids_sequence') as id";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		return rs.getLong("id");
	}

	@Override
	public long getNextCheckInCheckOutId() throws SQLException {
		
		String query = "select nextval('check_in_check_out_ids_sequence') as id";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		return rs.getLong("id");
	}

	@Override
	public long getNextCourseId() throws SQLException {
		
		String query = "select nextval('course_ids_sequence') as id";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		return rs.getLong("id");
	}

	@Override
	public long getNextLocationId() throws SQLException {
		
		String query = "select nextval('location_ids_sequence') as id";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		return rs.getLong("id");
	}

}
