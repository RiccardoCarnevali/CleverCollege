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
import com.clevercollege.persistence.dao.AdministratorDao;

public class AdministratorDaoJDBC implements AdministratorDao {

	private Connection conn;

	public AdministratorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<User> findAll() throws SQLException {
		List<User> administrators = new ArrayList<>();

		String query = "select * from administrators order by cf";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {
			User admin = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("cf"));
			administrators.add(admin);
		}

		return administrators;
	}

	@Override
	public User findByPrimaryKey(String cf) throws SQLException {
		User admin = null;

		String query = "select * from administrators where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			admin = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cf);
		}

		return admin;
	}

	@Override
	public void saveOrUpdate(User admin) throws SQLException {
		String query = "select * from administrators where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, admin.getCf());

		ResultSet rs = st.executeQuery();

		DatabaseManager.getInstance().getUserDao().saveOrUpdate(admin);

		if (!rs.next()) {
			query = "insert into administrators values(?)";

			PreparedStatement insertSt = conn.prepareStatement(query);

			insertSt.setString(1, admin.getCf());

			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(String cf) throws SQLException {

		String query = "delete from administrators where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		st.executeUpdate();

		DatabaseManager.getInstance().getUserDao().delete(cf);
	}

}
