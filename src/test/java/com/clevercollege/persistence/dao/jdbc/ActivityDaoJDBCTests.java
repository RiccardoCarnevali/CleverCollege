package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.Activity;
import com.clevercollege.persistence.DatabaseManager;

public class ActivityDaoJDBCTests {
	
	@Test
	public void findAllWorks() {
		List<Activity> activitiesFromDb = null;
		try {
			activitiesFromDb = DatabaseManager.getInstance().getActivityDao().findAll(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Activity> activities = new ArrayList<>();
		
		activities.add(new Activity(4, null, 0, null, null, null, null));
		activities.add(new Activity(5, null, 0, null, null, null, null));
		activities.add(new Activity(6, null, 0, null, null, null, null));
		
		assertArrayEquals(activities.toArray(), activitiesFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		Activity activityFromDb1 = null;
		Activity activityFromDb2 = null;
		try {
			activityFromDb1 = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(4, false);
			activityFromDb2 = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(1, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Activity activity = new Activity(4, null, 0, null, null, null, null);
		
		assertEquals(activity, activityFromDb1);
		assertNull(activityFromDb2);
	}
}
