
package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Activity;
import com.clevercollege.model.Lesson;
import com.clevercollege.model.LessonProxy;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.LessonDao;

public class LessonDaoJDBC implements LessonDao {

	private Connection conn;
	
	public LessonDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<Lesson> findAll(boolean lazy) throws SQLException {

		List<Lesson> lessons = new ArrayList<>();
		
		String query = "select * from lessons";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			
			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			Lesson lesson;
			
			if(lazy) {
				lesson = new LessonProxy();
			}
			else {
				lesson = new Lesson();
				lesson.setBookers(activity.getBookers());
			}
			
			lesson.setId(activity.getId());
			lesson.setTime(activity.getTime());
			lesson.setLength(activity.getLength());
			lesson.setDescription(activity.getDescription());
			lesson.setManager(activity.getManager());
			lesson.setClassroom(activity.getClassroom());
			lesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
			
			lessons.add(lesson);
		}
		
		return lessons;
	}

	@Override
	public Lesson findByPrimaryKey(long id, boolean lazy) throws SQLException {
		
		Lesson lesson = null;
		
		String query = "select * from lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			
			if(lazy) {
				lesson = new LessonProxy();
			}
			else {
				lesson = new Lesson();
				lesson.setBookers(activity.getBookers());
			}
			
			
			lesson.setId(activity.getId());
			lesson.setTime(activity.getTime());
			lesson.setLength(activity.getLength());
			lesson.setDescription(activity.getDescription());
			lesson.setManager(activity.getManager());
			lesson.setClassroom(activity.getClassroom());
			lesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
		}
		
		return lesson;
	}

	@Override
	public void saveOrUpdate(Lesson lesson) throws SQLException {
		
		String query = "select * from lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, lesson.getId());
		
		ResultSet rs = st.executeQuery();
		
		DatabaseManager.getInstance().getActivityDao().saveOrUpdate(lesson);
		
		if(rs.next()) {
			
			query = "update lessons set"
					+ "course = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setLong(1, lesson.getCourse().getId());
			updateSt.setLong(2, lesson.getId());
			
			updateSt.executeUpdate();
		}
		else {
			
			query = "insert into lessons values(?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, lesson.getId());
			insertSt.setLong(2, lesson.getCourse().getId());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {

		String query = "delete from lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
		
		DatabaseManager.getInstance().getActivityDao().delete(id);
	}

}
