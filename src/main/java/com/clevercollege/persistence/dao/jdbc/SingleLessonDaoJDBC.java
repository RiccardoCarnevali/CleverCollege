package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Lesson;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.SingleLessonProxy;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.SingleLessonDao;

public class SingleLessonDaoJDBC implements SingleLessonDao {

	private Connection conn;
	
	public SingleLessonDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<SingleLesson> findAll(boolean lazy) throws SQLException {

		List<SingleLesson> singleLessons = new ArrayList<>();
		
		String query = "select * from single_lessons";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			
			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			SingleLesson singleLesson;
			
			if(lazy) {
				singleLesson = new SingleLessonProxy();
			}
			else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}
			
			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
			singleLesson.setDate(rs.getDate("lesson_date"));
			
			singleLessons.add(singleLesson);
		}
		
		return singleLessons;
	}

	@Override
	public SingleLesson findByPrimaryKey(long id, boolean lazy) throws SQLException {
		
		SingleLesson singleLesson = null;
		
		String query = "select * from single_lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			if(lazy) {
				singleLesson = new SingleLessonProxy();
			}
			else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}
						
			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
			singleLesson.setDate(rs.getDate("lesson_date"));
		}
		
		return singleLesson;
	}

	@Override
	public void saveOrUpdate(SingleLesson singleLesson) throws SQLException {
		
		String query = "select * from single_lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, singleLesson.getId());
		
		ResultSet rs = st.executeQuery();
		
		DatabaseManager.getInstance().getLessonDao().saveOrUpdate(singleLesson);
		
		if(rs.next()) {
			
			query = "update single_lessons set"
					+ "lesson_date = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setDate(1, singleLesson.getDate());
			updateSt.setLong(2, singleLesson.getId());
			
			updateSt.executeUpdate();
		}
		else {
			
			query = "insert into single_lessons values(?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, singleLesson.getId());
			insertSt.setDate(2, singleLesson.getDate());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {

		String query = "delete from single_lessons where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
		
		DatabaseManager.getInstance().getLessonDao().delete(id);
	}

}
