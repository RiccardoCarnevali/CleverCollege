package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Lesson;
import com.clevercollege.model.WeeklyLesson;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.WeeklyLessonDao;

public class WeeklyLessonDaoJDBC implements WeeklyLessonDao {

	private Connection conn;
	
	public WeeklyLessonDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<WeeklyLesson> findAll() throws SQLException {

		List<WeeklyLesson> weeklyLessons = new ArrayList<>();
		
		String query = "select * from weekly_lessons";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			
			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), true);
			
			WeeklyLesson weeklyLesson = new WeeklyLesson();
			
			weeklyLesson.setId(lesson.getId());
			weeklyLesson.setTime(lesson.getTime());
			weeklyLesson.setLength(lesson.getLength());
			weeklyLesson.setDescription(lesson.getDescription());
			weeklyLesson.setManager(lesson.getManager());
			weeklyLesson.setClassroom(lesson.getClassroom());
			weeklyLesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
			weeklyLesson.setWeekDay(rs.getInt("week_day"));
			weeklyLesson.setDisabled(rs.getBoolean("is_disabled"));
			weeklyLesson.setDisabledIndefinitely(rs.getBoolean("disabled_indefinitely"));
			
			weeklyLessons.add(weeklyLesson);
		}
		
		return weeklyLessons;
	}

	@Override
	public WeeklyLesson findByPrimaryKey(long id) throws SQLException {
		
		WeeklyLesson weeklyLesson = null;
		
		String query = "select * from weekly_lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), true);
			
			weeklyLesson = new WeeklyLesson();
			
			weeklyLesson.setId(lesson.getId());
			weeklyLesson.setTime(lesson.getTime());
			weeklyLesson.setLength(lesson.getLength());
			weeklyLesson.setDescription(lesson.getDescription());
			weeklyLesson.setManager(lesson.getManager());
			weeklyLesson.setClassroom(lesson.getClassroom());
			weeklyLesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
			weeklyLesson.setWeekDay(rs.getInt("week_day"));
			weeklyLesson.setDisabled(rs.getBoolean("is_disabled"));
			weeklyLesson.setDisabledIndefinitely(rs.getBoolean("disabled_indefinitely"));
		}
		
		return weeklyLesson;
	}

	@Override
	public void saveOrUpdate(WeeklyLesson weeklyLesson) throws SQLException {
		
		String query = "select * from weekly_lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, weeklyLesson.getId());
		
		ResultSet rs = st.executeQuery();
		
		DatabaseManager.getInstance().getLessonDao().saveOrUpdate(weeklyLesson);
		
		if(rs.next()) {
			
			query = "update weekly_lessons set"
					+ "week_day = ?,"
					+ "is_disabled = ?,"
					+ "disabled_indefinitely = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setInt(1, weeklyLesson.getWeekDay());
			updateSt.setBoolean(2, weeklyLesson.isDisabled());
			updateSt.setBoolean(3, weeklyLesson.isDisabledIndefinitely());
			updateSt.setLong(4, weeklyLesson.getId());
			
			updateSt.executeUpdate();
		}
		else {
			
			query = "insert into weekly_lessons values(?,?,?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, weeklyLesson.getId());
			insertSt.setInt(2, weeklyLesson.getWeekDay());
			insertSt.setBoolean(3, weeklyLesson.isDisabled());
			insertSt.setBoolean(4, weeklyLesson.isDisabledIndefinitely());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {

		String query = "delete from weekly_lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
	}

}
