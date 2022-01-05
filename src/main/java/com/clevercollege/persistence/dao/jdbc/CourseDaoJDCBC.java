package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Course;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.CourseDao;

public class CourseDaoJDCBC implements CourseDao {

	private Connection conn;

	public CourseDaoJDCBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<Course> findAll() throws SQLException {

		List<Course> courses = new ArrayList<>();

		String query = "select * from courses order by id";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {

			Course course = new Course();

			course.setId(rs.getLong("id"));
			course.setName(rs.getString("course_name"));
			course.setLecturer(
					DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));

			courses.add(course);
		}

		return courses;
	}
	
	@Override
	public List<Course> findByLike(String like, int amount, int offset) throws SQLException {

		List<Course> courses = new ArrayList<>();

		String query = "select * from courses C, users U where C.professor = U.cf and (upper(course_name) like upper(?) or upper(U.first_name) || ' ' || upper(U.last_name) like upper(?)) order by course_name limit ? offset ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, like);
		st.setString(2, like);
		st.setInt(3, amount);
		st.setInt(4, offset);
		
		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Course course = new Course();

			course.setId(rs.getLong("id"));
			course.setName(rs.getString("course_name"));
			course.setLecturer(
					DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));

			courses.add(course);
		}

		return courses;
	}
	
	@Override
	public List<Course> findByProfessor(String professor) throws SQLException {
		
		List<Course> courses = new ArrayList<>();
		
		String query = "select * from courses where professor = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, professor);
		
		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Course course = new Course();

			course.setId(rs.getLong("id"));
			course.setName(rs.getString("course_name"));
			course.setLecturer(
					DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));

			courses.add(course);
		}

		return courses;
	}

	@Override
	public Course findByPrimaryKey(long id) throws SQLException {

		Course course = null;
		
		String query = "select * from courses where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, id);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			
			course = new Course();
			
			course.setId(rs.getLong("id"));
			course.setName(rs.getString("course_name"));
			course.setLecturer(
					DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));
		}
		
		return course;
	}

	@Override
	public void saveOrUpdate(Course course) throws SQLException {
		
		String query = "select * from courses where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, course.getId());

		ResultSet rs = st.executeQuery();

		if(rs.next()) {
			
			query = "update courses set "
					+ "course_name = ?,"
					+ "professor = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setString(1, course.getName());
			updateSt.setString(2, course.getLecturer().getCf());
			updateSt.setLong(3, course.getId());
			
			updateSt.executeUpdate();
			
		}
		else {
			
			query = "insert into courses values(?,?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, course.getId());
			insertSt.setString(2, course.getName());
			insertSt.setString(3, course.getLecturer().getCf());
			
			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {
		
		String query = "delete from courses where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();
	}

	@Override
	public Course findByNameAndProfessor(String name, String cf) throws SQLException {
		Course course = null;

		String query = "select * from courses where professor = ? and course_name = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);
		st.setString(2, name);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			course = new Course();

			course.setId(rs.getLong("id"));
			course.setName(rs.getString("course_name"));
			course.setLecturer(
					DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));

		}

		return course;
	}

}
