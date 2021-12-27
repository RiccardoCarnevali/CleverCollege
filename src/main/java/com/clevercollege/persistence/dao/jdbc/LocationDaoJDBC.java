package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Location;
import com.clevercollege.persistence.dao.LocationDao;

public class LocationDaoJDBC implements LocationDao {

	private Connection conn;

	public LocationDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<Location> findAll() throws SQLException {

		List<Location> locations = new ArrayList<>();

		String query = "select * from locations order by id";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {

			Location location = new Location();

			location.setId(rs.getLong("id"));
			location.setName(rs.getString("location_name"));
			location.setCapacity(rs.getInt("capacity"));

			locations.add(location);
		}

		return locations;
	}

	@Override
	public Location findByPrimaryKey(long id) throws SQLException {

		Location location = null;

		String query = "select * from locations where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, id);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {

			location = new Location();

			location.setId(rs.getLong("id"));
			location.setName(rs.getString("location_name"));
			location.setCapacity(rs.getInt("capacity"));

		}

		return location;
	}

	@Override
	public void saveOrUpdate(Location location) throws SQLException {
	
		String query = "select * from locations where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, location.getId());
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			query = "update locations set "
					+ "location_name = ?,"
					+ "capacity = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setString(1, location.getName());
			updateSt.setInt(2, location.getCapacity());
			updateSt.setLong(3, location.getId());
			
			updateSt.executeUpdate();
		}
		else {
			
			query = "insert into locations values(?,?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, location.getId());
			insertSt.setString(2, location.getName());
			insertSt.setInt(3, location.getCapacity());
			
			insertSt.executeUpdate();
		}
		
	}

	@Override
	public void delete(long id) throws SQLException {
		
		String query = "delete from locations where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
		
	}

}
