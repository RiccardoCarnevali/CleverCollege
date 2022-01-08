package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Activity;
import com.clevercollege.model.ActivityProxy;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.WeeklyLesson;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.ActivityDao;

public class ActivityDaoJDBC implements ActivityDao {

	private Connection conn;

	public ActivityDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<Activity> findAll(boolean lazy) throws SQLException {
		List<Activity> activities = new ArrayList<>();

		String query = "select * from activities order by id";

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {
			
			Activity activity;
			
			if(lazy) {
				activity = new ActivityProxy();
			}
			else {
				activity = new Activity();
				activity.setBookers(DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong("id")));
			}

			activity.setId(rs.getLong("id"));
			activity.setTime(rs.getTime("activity_time"));
			activity.setLength(rs.getInt("activity_length"));
			activity.setDescription(rs.getString("description"));
			activity.setManager(
					DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));
			activity.setClassroom(
					DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong("classroom")));

			activities.add(activity);
		}

		return activities;
	}

	@Override
	public Activity findByPrimaryKey(long id, boolean lazy) throws SQLException {

		Activity activity = null;

		String query = "select * from activities where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, id);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			
			if(lazy) {
				activity = new ActivityProxy();
			}
			else {
				activity = new Activity();
				activity.setBookers(DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong("id")));
			}

			activity.setId(rs.getLong("id"));
			activity.setTime(rs.getTime("activity_time"));
			activity.setLength(rs.getInt("activity_length"));
			activity.setDescription(rs.getString("description"));
			activity.setManager(
					DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));
			activity.setClassroom(
					DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong("classroom")));
		}

		return activity;
	}

	@Override
	public void saveOrUpdate(Activity activity) throws SQLException {

		String query = "select * from activities where id = ?";

		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, activity.getId());
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			query = "update activities set "
					+ "activity_time = ?,"
					+ "activity_length = ?,"
					+ "description = ?,"
					+ "professor = ?,"
					+ "classroom = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setTime(1, activity.getTime());
			updateSt.setInt(2, activity.getLength());
			updateSt.setString(3, activity.getDescription());
			updateSt.setString(4, activity.getManager().getCf());
			updateSt.setLong(5, activity.getClassroom().getId());
			updateSt.setLong(6, activity.getId());
			
			updateSt.executeUpdate();
		}
		else {
			query = "insert into activities values(?,?,?,?,?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, activity.getId());
			insertSt.setTime(2, activity.getTime());
			insertSt.setInt(3, activity.getLength());
			insertSt.setString(4, activity.getDescription());
			insertSt.setString(5, activity.getManager().getCf());
			insertSt.setLong(6, activity.getClassroom().getId());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {
		
		String query = "delete from activities where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
	}

	@Override
	public List<Activity> findByStudentBooked(String studentCf) throws SQLException {
		List<Activity> activities = new ArrayList<>();
		String query = "select A.id" +
					  " from books, activities A" +
					  " where books.student = ? and books.activity = A.id";
		
		PreparedStatement st = conn.prepareStatement(query);
		st.setString(1, studentCf);
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Seminar s = DatabaseManager.getInstance().getSeminarDao().findByPrimaryKey(rs.getLong("id"), true);
			SingleLesson sl = DatabaseManager.getInstance().getSingleLessonDao().findByPrimaryKey(rs.getLong("id"), true);
			WeeklyLesson wl = DatabaseManager.getInstance().getWeeklyLessonDao().findByPrimaryKey(rs.getLong("id"));
					
			if (wl != null) {
				activities.add(wl);				
			}
			else if (sl != null) {
				activities.add(sl);
			}
			else if (s != null) {
				activities.add(s);
			}
		}
		
		return activities;
	}

}
