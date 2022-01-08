package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.ProfessorDao;

public class ProfessorDaoJDBC implements ProfessorDao {

	private Connection conn;

	public ProfessorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<User> findAll() throws SQLException {
		List<User> professors = new ArrayList<>();
		
		String query = "select * from professors order by cf";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			User professor = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("cf"));
			professors.add(professor);
		}
		
		return professors;
	}
	
	@Override
	public List<User> findByLike(String sortBy, String like, int amount, int offset) throws SQLException {
		List<User> professors = new ArrayList<>();

		String query = "select * from professors P, users U where P.cf = U.cf and (upper(U.first_name) || ' ' || upper(U.last_name) like upper(?)) order by u." + sortBy + " limit ? offset ?";

		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, like);
		st.setInt(2, amount);
		st.setInt(3, offset);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			User professor = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("cf"));
			professors.add(professor);
		}

		return professors;
	}

	@Override
	public User findByPrimaryKey(String cf) throws SQLException {
		User professor = null;

		String query = "select * from professors where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			professor = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cf);
		}

		return professor;	
	}

	@Override
	public void saveOrUpdate(User professor) throws SQLException {
		String query = "select * from professors where cf = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, professor.getCf());
		
		ResultSet rs = st.executeQuery();
		
		DatabaseManager.getInstance().getUserDao().saveOrUpdate(professor);
		
		if(!rs.next()) {
			query = "insert into professors values(?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setString(1, professor.getCf());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(String cf) throws SQLException {
		
		String query = "delete from professors where cf = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, cf);
		
		st.executeUpdate();
		
		DatabaseManager.getInstance().getUserDao().delete(cf);
	}
}
