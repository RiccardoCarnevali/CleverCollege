package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.CheckInCheckOut;
import com.clevercollege.model.Location;
import com.clevercollege.model.Student;
import com.clevercollege.model.User;
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
		
		String query = "select * from check_in_check_out order by id";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			
			CheckInCheckOut checkInCheckOut = new CheckInCheckOut();
			
			checkInCheckOut.setId(rs.getLong("id"));
			checkInCheckOut.setInTime(rs.getTime("in_time").toString());
			checkInCheckOut.setOutTime(rs.getTime("out_time").toString());
			checkInCheckOut.setUser(DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("c_user")));
			checkInCheckOut.setLocation(DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(rs.getLong("c_location")));
			checkInCheckOut.setDate(rs.getDate("c_date").toString());
			
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
			checkInCheckOut.setInTime(rs.getTime("in_time").toString());
			checkInCheckOut.setOutTime(rs.getTime("out_time").toString());
			checkInCheckOut.setUser(DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("c_user")));
			checkInCheckOut.setLocation(DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(rs.getLong("c_location")));
			checkInCheckOut.setDate(rs.getDate("c_date").toString());
		
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
			
			query = "update check_in_check_out set "
					+ "in_time = ?,"
					+ "out_time = ?,"
					+ "c_user = ?,"
					+ "c_location = ?,"
					+ "c_date = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setTime(1, Time.valueOf(checkInCheckOut.getInTime()));
			updateSt.setTime(2, checkInCheckOut.getOutTime() == null ? null : Time.valueOf(checkInCheckOut.getOutTime()));
			updateSt.setString(3, checkInCheckOut.getUser().getCf());
			updateSt.setLong(4, checkInCheckOut.getLocation().getId());
			updateSt.setDate(5, Date.valueOf(checkInCheckOut.getDate()));
			updateSt.setLong(6, checkInCheckOut.getId());
			
			updateSt.executeUpdate();
			
		}
		else {
			
			query = "insert into check_in_check_out values(?,?,?,?,?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, checkInCheckOut.getId());
			insertSt.setTime(2, Time.valueOf(checkInCheckOut.getInTime()));
			insertSt.setTime(3, checkInCheckOut.getOutTime() == null ? null : Time.valueOf(checkInCheckOut.getOutTime()));
			insertSt.setString(4, checkInCheckOut.getUser().getCf());
			insertSt.setLong(5, checkInCheckOut.getLocation().getId());
			insertSt.setDate(6, Date.valueOf(checkInCheckOut.getDate()));
			
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
	
	@Override
	public List<CheckInCheckOut> findByUser(String cf) throws SQLException {
		List<CheckInCheckOut> checkInCheckOuts = new ArrayList<CheckInCheckOut>();
		
		String query = "select * from check_in_check_out where c_user = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, cf);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			CheckInCheckOut checkInCheckOut = new CheckInCheckOut();
			
			checkInCheckOut.setId(rs.getLong("id"));
			checkInCheckOut.setInTime(rs.getTime("in_time").toString());
			checkInCheckOut.setOutTime(rs.getTime("out_time").toString());
			checkInCheckOut.setDate(rs.getDate("c_date").toString());
			checkInCheckOut.setUser(DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("c_user")));
			checkInCheckOut.setLocation(DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(rs.getLong("c_location")));
		
			checkInCheckOuts.add(checkInCheckOut);
		}
		
		return checkInCheckOuts;
	}

	@Override
	public CheckInCheckOut findActiveByUser(String cf) throws SQLException{
		CheckInCheckOut checkInCheckOut = null;
		
		String query = "select * from check_in_check_out where c_user = ? and out_time is null";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, cf);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			checkInCheckOut = new CheckInCheckOut();
			
			checkInCheckOut.setId(rs.getLong("id"));
			checkInCheckOut.setInTime(rs.getTime("in_time").toString());
			checkInCheckOut.setDate(rs.getDate("c_date").toString());
			checkInCheckOut.setUser(DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cf));
			checkInCheckOut.setLocation(DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(rs.getLong("c_location")));
		}
		
		return checkInCheckOut;
	}


	@Override
	public Location findPlaceOfCheckIn(String cfUser) throws SQLException {
		Location l = null;

		String query = "select l.id, l.location_name, l.capacity from check_in_check_out as c, locations as l where c.c_location = l.id and c.c_user = ? and c.out_time is null";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cfUser);

		ResultSet rs = st.executeQuery();

		if(rs.next()) {

			l = new Location();

			l.setId(rs.getLong("id"));
			l.setName(rs.getString("location_name"));
			l.setCapacity(rs.getInt("capacity"));
		}

		return l;
	}

	@Override
	public List<Student> findCheckInStudentsByLocation(Long idLocation) throws SQLException{
		List<Student> checkedInStudents = new ArrayList<>();

		String query = "select s.cf, s.student_number from students as s , check_in_check_out as c " +
				"where s.cf = c.c_user and c.c_location = ? and c.out_time is null";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, idLocation);

		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			Student s = new Student();
			User user = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString(1));
			s.setCf(user.getCf());
			s.setFirstName(user.getFirstName());
			s.setLastName(user.getLastName());
			s.setEmail(user.getEmail());
			s.setPassword(user.getPassword());
			s.setDescription(user.getDescription());
			s.setProfilePicture(user.getProfilePicture());
			s.setStudentNumber(rs.getString(2));
			checkedInStudents.add(s);
		}

		return checkedInStudents;
	}
}
