package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.CheckInCheckOut;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.CheckInCheckOutDao;

public class CheckInCheckOutDaoJDBC implements CheckInCheckOutDao {

	private Connection conn;
	
	public CheckInCheckOutDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<CheckInCheckOut> findAll() throws SQLException {

		List<CheckInCheckOut> checkInCheckOuts = new ArrayList<>();
		
		String query = "select * from check_in_check_out";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			
			CheckInCheckOut checkInCheckOut = new CheckInCheckOut();
			
			checkInCheckOut.setId(rs.getLong("id"));
			checkInCheckOut.setInTime(rs.getTime("in_time"));
			checkInCheckOut.setOutTime(rs.getTime("out_time"));
			checkInCheckOut.setUser(DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("c_user")));
			checkInCheckOut.setLocation(DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(rs.getLong("c_location")));
			
			checkInCheckOuts.add(checkInCheckOut);
		}
		
		return checkInCheckOuts;
	}

	@Override
	public CheckInCheckOut findByPrimaryKey(long id) throws SQLException {

		CheckInCheckOut checkInCheckOut = null;
		
		String query = "select * from check_in_check_out where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			checkInCheckOut = new CheckInCheckOut();
			
			checkInCheckOut.setId(rs.getLong("id"));
			checkInCheckOut.setInTime(rs.getTime("in_time"));
			checkInCheckOut.setOutTime(rs.getTime("out_time"));
			checkInCheckOut.setUser(DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("c_user")));
			checkInCheckOut.setLocation(DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(rs.getLong("c_location")));
		}
		
		return checkInCheckOut;
	}

	@Override
	public void saveOrUpdate(CheckInCheckOut checkInCheckOut) throws SQLException {

		String query = "select * from check_in_check_out where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, checkInCheckOut.getId());
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			query = "update check_in_check_out set"
					+ "in_time = ?,"
					+ "out_time = ?,"
					+ "c_user = ?,"
					+ "c_location = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setTime(1, checkInCheckOut.getInTime());
			updateSt.setTime(2, checkInCheckOut.getOutTime());
			updateSt.setString(3, checkInCheckOut.getUser().getCf());
			updateSt.setLong(4, checkInCheckOut.getLocation().getId());
			updateSt.setLong(5, checkInCheckOut.getId());
			
			updateSt.executeQuery();
			
		}
		else {
			
			query = "insert into check_in_check_out values(?,?,?,?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, checkInCheckOut.getId());
			insertSt.setTime(2, checkInCheckOut.getInTime());
			insertSt.setTime(3, checkInCheckOut.getOutTime());
			insertSt.setString(4, checkInCheckOut.getUser().getCf());
			insertSt.setLong(5, checkInCheckOut.getLocation().getId());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {

		String query = "delete from check_in_check_out where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
	}

}
