package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.NotificationToken;

public interface NotificationTokenDao {
	
	public List<NotificationToken> findAll() throws SQLException;
	
	public boolean findToken(NotificationToken notificationToken) throws SQLException;
	
	public List<NotificationToken> findTokensForActivity(long activityId) throws SQLException;
	
	public List<NotificationToken> findTokensForActivityStillInClassroom(long activityId) throws SQLException;

	public void save(NotificationToken notificationToken) throws SQLException;
	
	public void delete(NotificationToken notificationToken) throws SQLException;
}
