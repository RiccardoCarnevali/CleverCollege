package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.User;
import com.clevercollege.persistence.dao.UserDao;

public class UserDaoJDBC implements UserDao {

	private Connection conn;

	public UserDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<User> findAll() throws SQLException {
		List<User> users = new ArrayList<>();

		String query = "select * from users order by cf";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {

			User user = new User();

			user.setCf(rs.getString("cf"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setEmail(rs.getString("e_mail"));
			user.setPassword(rs.getString("hashed_password"));
			user.setDescription(rs.getString("description"));
			user.setProfilePicture(rs.getString("profile_picture"));

			users.add(user);
		}

		return users;
	}

	@Override
	public User findByPrimaryKey(String cf) throws SQLException {

		User user = null;

		String query = "select * from users where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			user = new User();

			user.setCf(rs.getString("cf"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setEmail(rs.getString("e_mail"));
			user.setPassword(rs.getString("hashed_password"));
			user.setDescription(rs.getString("description"));
			user.setProfilePicture(rs.getString("profile_picture"));
		}

		return user;
	}

	@Override
	public void saveOrUpdate(User user) throws SQLException {

		String query = "select * from users where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);
		st.setString(1, user.getCf());
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			query = "update users set "
					+ "first_name = ?,"
					+ "last_name = ?,"
					+ "e_mail = ?,"
					+ "hashed_password = ?,"
					+ "description = ?,"
					+ "profile_picture = ?"
					+ "where cf = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			updateSt.setString(1, user.getFirstName());
			updateSt.setString(2, user.getLastName());
			updateSt.setString(3, user.getEmail());
			updateSt.setString(4, user.getPassword());
			updateSt.setString(5, user.getDescription());
			updateSt.setString(6, user.getProfilePicture());
			updateSt.setString(7, user.getCf());
			
			updateSt.executeUpdate();
		}
		else {
			query = "insert into users values(?,?,?,?,?,?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setString(1, user.getCf());
			insertSt.setString(2, user.getFirstName());
			insertSt.setString(3, user.getLastName());
			insertSt.setString(4, user.getEmail());
			insertSt.setString(5, user.getPassword());
			insertSt.setString(6, user.getDescription());
			insertSt.setString(7, user.getProfilePicture());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(String cf) throws SQLException {
				
		String query = "delete from users where cf = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, cf);
		
		st.executeUpdate();
	}

}
