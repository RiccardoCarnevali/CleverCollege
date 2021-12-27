package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Location;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.ClassroomDao;

public class ClassroomDaoJDBC implements ClassroomDao {

	private Connection conn;

	public ClassroomDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<Location> findAll() throws SQLException {
		List<Location> classrooms = new ArrayList<>();

		String query = "select * from classrooms";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {

			Location classroom = DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(rs.getLong("id"));
			classrooms.add(classroom);
		}

		return classrooms;
	}

	@Override
	public Location findByPrimaryKey(long id) throws SQLException {
		Location classroom = null;

		String query = "select * from classrooms where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, id);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {

			classroom = DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(id);

		}

		return classroom;
	}

	@Override
	public void saveOrUpdate(Location classroom) throws SQLException {
		String query = "select * from classrooms where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, classroom.getId());
		
		ResultSet rs = st.executeQuery();
		
		DatabaseManager.getInstance().getLocationDao().saveOrUpdate(classroom);
		
		if(!rs.next()) {
			
			query = "insert into classrooms values(?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, classroom.getId());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {
		String query = "delete from classrooms where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
		
		DatabaseManager.getInstance().getLocationDao().delete(id);
	}

}
