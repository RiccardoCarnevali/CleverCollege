package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.NotificationToken;
import com.clevercollege.persistence.dao.NotificationTokenDao;

public class NotificationTokenDaoJDBC implements NotificationTokenDao {

	private Connection conn;
	
	public NotificationTokenDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<NotificationToken> findAll() throws SQLException {
		
		List<NotificationToken> notificationTokens = new ArrayList<>();
		
		String query = "select * from notification_tokens where enabled = true";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			NotificationToken notificationToken = new NotificationToken();
			
			notificationToken.setToken(rs.getString("notification_token"));
			notificationToken.setUser(rs.getString("notification_user"));
			
			notificationTokens.add(notificationToken);
		}
		
		return notificationTokens;
	}
	
	@Override
	public List<NotificationToken> findByUser(String user) throws SQLException {
		
		List<NotificationToken> notificationTokens = new ArrayList<>();

		String query = "select * from notification_tokens where notification_user = ? and enabled = true";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, user);
		
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			NotificationToken notificationToken = new NotificationToken();
			
			notificationToken.setToken(rs.getString("notification_token"));
			notificationToken.setUser(rs.getString("notification_user"));
			
			notificationTokens.add(notificationToken);
		}
		
		return notificationTokens;
	}
	
	@Override
	public boolean findToken(NotificationToken notificationToken) throws SQLException {
		String query = "select * from notification_tokens where notification_token = ? and notification_user = ? and enabled = true";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, notificationToken.getToken());
		st.setString(2, notificationToken.getUser());
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next())
			return true;
		
		return false;
	}
	
	@Override
	public List<NotificationToken> findTokensForActivity(long activityId) throws SQLException {
		
		List<NotificationToken> notificationTokens = new ArrayList<>();
		
		String query = "select notification_token, notification_user "
						+ "from notification_tokens NT, books B "
						+ "where NT.notification_user = B.student and B.activity = ? and NT.enabled = true";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, activityId);
		
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			NotificationToken notificationToken = new NotificationToken();
			
			notificationToken.setToken(rs.getString("notification_token"));
			notificationToken.setUser(rs.getString("notification_user"));
			
			notificationTokens.add(notificationToken);
		}
		
		query = "select notification_token, notification_user "
				+ "from notification_tokens NT, activities A "
				+ "where NT.notification_user = A.professor and A.id = ? and NT.enabled = true";
		
		st = conn.prepareStatement(query);
		
		st.setLong(1, activityId);
		
		rs = st.executeQuery();
		
		while(rs.next()) {
			NotificationToken notificationToken = new NotificationToken();
			
			notificationToken.setToken(rs.getString("notification_token"));
			notificationToken.setUser(rs.getString("notification_user"));
			
			notificationTokens.add(notificationToken);
		}
		
		return notificationTokens;
	}
	
	@Override
	public List<NotificationToken> findTokensForActivityStillInClassroom(long activityId) throws SQLException {

		
		List<NotificationToken> notificationTokens = new ArrayList<>();
		
		String query = "select notification_token, notification_user "
						+ "from notification_tokens NT, books B, check_in_check_out C "
						+ "where NT.notification_user = B.student and B.student = C.c_user and B.activity = ? and C.out_time is null and NT.enabled = true";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, activityId);
		
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			NotificationToken notificationToken = new NotificationToken();
			
			notificationToken.setToken(rs.getString("notification_token"));
			notificationToken.setUser(rs.getString("notification_user"));
			
			notificationTokens.add(notificationToken);
		}
		
		query = "select notification_token, notification_user "
				+ "from notification_tokens NT, activities A, check_in_check_out C "
				+ "where NT.notification_user = A.professor and A.professor = C.c_user and A.id = ? and C.out_time is null and NT.enabled = true";
		
		st = conn.prepareStatement(query);
		
		st.setLong(1, activityId);
		
		rs = st.executeQuery();
		
		while(rs.next()) {
			NotificationToken notificationToken = new NotificationToken();
			
			notificationToken.setToken(rs.getString("notification_token"));
			notificationToken.setUser(rs.getString("notification_user"));
			
			notificationTokens.add(notificationToken);
		}
		
		return notificationTokens;
	}
	
	@Override
	public void saveOrUpdate(NotificationToken notificationToken) throws SQLException {
		
		String query = "select * from notification_tokens where notification_token = ? and notification_user = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, notificationToken.getToken());
		st.setString(2, notificationToken.getUser());
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			query = "update notification_tokens set "
					+ "enabled = ? "
					+ "where notification_token = ? and notification_user = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setBoolean(1, notificationToken.isEnabled());
			updateSt.setString(2, notificationToken.getToken());
			updateSt.setString(3, notificationToken.getUser());
			
			updateSt.executeUpdate();
		}
		else {
			query = "insert into notification_tokens values(?, ?, ?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setString(1, notificationToken.getToken());
			insertSt.setString(2, notificationToken.getUser());
			insertSt.setBoolean(3, notificationToken.isEnabled());
			
			insertSt.executeUpdate();
		}
	}
	
	@Override
	public void delete(NotificationToken notificationToken) throws SQLException {
		String query = "delete from notification_tokens where notification_token = ? and notification_user = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, notificationToken.getToken());
		st.setString(2, notificationToken.getUser());
		
		st.executeUpdate();
	}
}
