package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Student;
import com.clevercollege.model.StudentProxy;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.StudentDao;

public class StudentDaoJDBC implements StudentDao {

	private Connection conn;
	
	public StudentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<Student> findAll(boolean lazy) throws SQLException {

		List<Student> students = new ArrayList<>();
		
		String query = "select * from students order by cf";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			User user = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("cf"));
			
			Student student;
			
			if(lazy) {
				student = new StudentProxy();
			}
			else {
				student = new Student();
				student.setFollowedCourses(DatabaseManager.getInstance().getCourseDao().findCoursesFollowedBy(user.getCf()));
			}
			
			student.setCf(user.getCf());
			student.setFirstName(user.getFirstName());
			student.setLastName(user.getLastName());
			student.setEmail(user.getEmail());
			student.setPassword(user.getPassword());
			student.setDescription(user.getDescription());
			student.setProfilePicture(user.getProfilePicture());
			student.setStudentNumber(rs.getString("student_number"));
			
			students.add(student);
		}
		
		return students;
	}
	
	@Override
	public List<User> findByLike(String sortBy, String like, int amount, int offset) throws SQLException {
		List<User> students = new ArrayList<>();

		String query = "select * from students S, users U where S.cf = U.cf and (upper(U.first_name) || ' ' || upper(U.last_name) like upper(?)) order by u." + sortBy + " limit ? offset ?";

		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, like);
		st.setInt(2, amount);
		st.setInt(3, offset);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			User student = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("cf"));
			students.add(student);
		}

		return students;
	}
	
	@Override
	public Student findByPrimaryKey(String cf, boolean lazy) throws SQLException {

		Student student = null;
		
		String query = "select * from students where cf = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, cf);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			User user = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cf);
			
			if(lazy) {
				student = new StudentProxy();
			}
			else {
				student = new Student();
				student.setFollowedCourses(DatabaseManager.getInstance().getCourseDao().findCoursesFollowedBy(user.getCf()));
			}
			
			student.setCf(user.getCf());
			student.setFirstName(user.getFirstName());
			student.setLastName(user.getLastName());
			student.setEmail(user.getEmail());
			student.setPassword(user.getPassword());
			student.setDescription(user.getDescription());
			student.setProfilePicture(user.getProfilePicture());
			student.setStudentNumber(rs.getString("student_number"));
		}
		
		return student;
	}

	@Override
	public void saveOrUpdate(Student student) throws SQLException {
		
		String query = "select * from students where cf = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, student.getCf());
		
		ResultSet rs = st.executeQuery();
				
		DatabaseManager.getInstance().getUserDao().saveOrUpdate(student);
		
		if(rs.next()) {	
			query = "update students set "
					+ "student_number = ?"
					+ "where cf = ?";
				
			PreparedStatement updateSt = conn.prepareStatement(query);
			updateSt.setString(1, student.getStudentNumber());
			updateSt.setString(2, student.getCf());
				
			updateSt.executeUpdate();
		}
		else {
			query = "insert into students values(?,?)";
				
			PreparedStatement insertSt = conn.prepareStatement(query);
				
			insertSt.setString(1, student.getCf());
			insertSt.setString(2, student.getStudentNumber());
				
			insertSt.executeUpdate();
		}		
	}

	@Override
	public void delete(String cf) throws SQLException {	
		
		String query = "delete from students where cf = ?";
			
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, cf);
			
		st.executeUpdate();
		
		DatabaseManager.getInstance().getUserDao().delete(cf);
	}

	@Override
	public List<Student> findBookersForActivity(long activityId, boolean lazy) throws SQLException {

		List<Student> students = new ArrayList<>();
		
		String query = "select * from books where activity = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, activityId);
		
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			students.add(findByPrimaryKey(rs.getString("student"), lazy));
		}
		
		return students;
	}

	@Override
	public boolean bookActivityForStudent(long activityId, String studentCf) throws SQLException {
		
		String query = "select capacity from activities A, locations L where A.classroom = L.id and A.id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, activityId);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			int capacity = rs.getInt("capacity");
			
			query = "select count(*) as bookings from books where activity = ?";
			
			st = conn.prepareStatement(query);
			
			st.setLong(1, activityId);
			
			rs = st.executeQuery();
			
			if(rs.next()) {

				if(rs.getInt("bookings") < capacity) {
					
					query = "insert into books values(?,?)";
					
					st = conn.prepareStatement(query);
					
					st.setString(1, studentCf);
					st.setLong(2, activityId);
					
					st.executeUpdate();
					
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void unbookActivityForStudent(long activityId, String studentCf) throws SQLException {
		
		String query = "delete from books where student = ? and activity = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, studentCf);
		st.setLong(2, activityId);
		
		st.executeUpdate();
	}
	
	@Override
	public Student findByIdStudent(String idStudent) throws SQLException {
		Student student = null;

		String query = "select * from students where student_number = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, idStudent);

		ResultSet rs = st.executeQuery();

		if(rs.next()) {
			student = new Student();

			User user = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("cf"));

			student.setCf(rs.getString("cf"));
			student.setFirstName(user.getFirstName());
			student.setLastName(user.getLastName());
			student.setEmail(user.getEmail());
			student.setPassword(user.getPassword());
			student.setDescription(user.getDescription());
			student.setProfilePicture(user.getProfilePicture());
			student.setStudentNumber(rs.getString("student_number"));
		}

		return student;
	}
	
	@Override
	public void followCourseForStudent(long courseId, String studentCf) throws SQLException {
		
		String query = "insert into follows values(?,?)";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, studentCf);
		st.setLong(2, courseId);
		
		st.executeUpdate();
	}
	
	@Override
	public void unfollowCourseForStudent(long courseId, String studentCf) throws SQLException {
		
		String query = "delete from follows where student = ? and course = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, studentCf);
		st.setLong(2, courseId);
		
		st.executeUpdate();
	}
}
