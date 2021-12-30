package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clevercollege.model.RecoveryToken;
import com.clevercollege.persistence.dao.RecoveryTokenDao;

public class RecoveryTokenDaoJDBC implements RecoveryTokenDao {

	private Connection conn;

	public RecoveryTokenDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public RecoveryToken findByPrimaryKey(String cf) throws SQLException {
		RecoveryToken token = null;

		String query = "select * from recovery_tokens where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			token = new RecoveryToken();

			token.setCf(rs.getString("cf"));
			token.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
			token.setId(rs.getLong("id"));
			token.setToken(rs.getString("token"));
		}
		return token;
	}

	@Override
	public void saveOrUpdate(RecoveryToken token) throws SQLException {
		String query = "select * from recovery_tokens where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);
		st.setString(1, token.getCf());

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			query = "update recovery_tokens set " 
					+ "token = ?," 
					+ "id = ?," 
					+ "expiry_date = ? "
					+ "where cf = ?";

			PreparedStatement updateSt = conn.prepareStatement(query);
			updateSt.setString(1, token.getToken());
			updateSt.setLong(2, token.getId());
			updateSt.setDate(3, Date.valueOf(token.getExpiryDate()));
			updateSt.setString(4, token.getCf());

			updateSt.executeUpdate();
		} else {
			query = "insert into recovery_tokens values(?,?,?,?)";

			PreparedStatement insertSt = conn.prepareStatement(query);

			insertSt.setString(1, token.getCf());
			insertSt.setString(2, token.getToken());
			insertSt.setLong(3, token.getId());
			insertSt.setDate(4, Date.valueOf(token.getExpiryDate()));

			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(String cf) throws SQLException {

		String query = "delete from recovery_tokens where cf = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		st.executeUpdate();
	}

}
