package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Activity;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SeminarProxy;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.SeminarDao;

public class SeminarDaoJDBC implements SeminarDao {

	private Connection conn;
	
	public SeminarDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<Seminar> findAll(boolean lazy) throws SQLException {
		
		List<Seminar> seminars = new ArrayList<>();
		
		String query = "select * from seminars order by id";
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			
			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			Seminar seminar;
			
			if(lazy) {
				seminar = new SeminarProxy();
			}
			else {
				seminar = new Seminar();
				seminar.setBookers(activity.getBookers());
			}
			
			seminar.setId(activity.getId());
			seminar.setTime(activity.getTime());
			seminar.setLength(activity.getLength());
			seminar.setDescription(activity.getDescription());
			seminar.setManager(activity.getManager());
			seminar.setClassroom(activity.getClassroom());
			seminar.setDate(rs.getDate("seminar_date").toString());

			seminars.add(seminar);
		}
		
		return seminars;
	}

	@Override
	public Seminar findByPrimaryKey(long id, boolean lazy) throws SQLException {
		
		Seminar seminar = null;
		
		String query = "select * from seminars where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		ResultSet rs = st.executeQuery();
		
		if(rs.next()) {
			
			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			if(lazy) {
				seminar = new SeminarProxy();
			}
			else {
				seminar = new Seminar();
				seminar.setBookers(activity.getBookers());
			}
						
			seminar.setId(activity.getId());
			seminar.setTime(activity.getTime());
			seminar.setLength(activity.getLength());
			seminar.setDescription(activity.getDescription());
			seminar.setManager(activity.getManager());
			seminar.setClassroom(activity.getClassroom());
			seminar.setDate(rs.getDate("seminar_date").toString());
		}
		
		return seminar;
	}

	@Override
	public void saveOrUpdate(Seminar seminar) throws SQLException {
		
		String query = "select * from seminars where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, seminar.getId());
		
		ResultSet rs = st.executeQuery();
		
		DatabaseManager.getInstance().getActivityDao().saveOrUpdate(seminar);
		
		if(rs.next()) {
			
			query = "update seminars set "
					+ "seminar_date = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setDate(1, Date.valueOf(seminar.getDate()));
			updateSt.setLong(2, seminar.getId());
			
			updateSt.executeUpdate();
		}
		else {
			
			query = "insert into seminars values(?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, seminar.getId());
			insertSt.setDate(2, Date.valueOf(seminar.getDate()));
			
			insertSt.executeUpdate();
		}		
	}

	@Override
	public void delete(long id) throws SQLException {

		String query = "delete from seminars where id = ?";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setLong(1, id);
		
		st.executeUpdate();		
		
		DatabaseManager.getInstance().getActivityDao().delete(id);
	}
	
	@Override
	public List<Seminar> findByProfessor(String cf, boolean lazy) throws SQLException {
		List<Seminar> seminars = new ArrayList<>();

		String query = "select * from seminars as x, activities as y where x.id = y.id and professor = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Seminar seminar;
			if (lazy) {
				seminar = new SeminarProxy();
			} else {
				seminar = new Seminar();
				seminar.setBookers(
						DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong("id"), true));
			}
			seminar.setId(rs.getLong("id"));
			seminar.setTime(rs.getTime("activity_time").toString());
			seminar.setDate(rs.getDate("seminar_date").toString());
			seminar.setLength(rs.getInt("activity_length"));
			seminar.setDescription(rs.getString("description"));
			seminar.setManager(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(cf));
			seminar.setClassroom(
					DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong("classroom")));

			seminars.add(seminar);
		}

		return seminars;
	}
	
	@Override
	public List<Seminar> findByCollidingTimeForStudent(String date, String time, int length, String studentCf, boolean lazy) throws SQLException {

		List<Seminar> seminars = new ArrayList<>();
		
		String query = "select * from books B, seminars S, activities A where B.activity = S.id and S.id = A.id and B.student = ? and " +
						"seminar_date = ? and ((activity_time <= ? and ? < activity_time + interval '1 min' * activity_length) or "
						+ "(? <= activity_time and activity_time < ?))";
		
		PreparedStatement st = conn.prepareStatement(query);
		
		st.setString(1, studentCf);
		st.setDate(2, Date.valueOf(date));
		st.setTime(3, Time.valueOf(time));
		st.setTime(4, Time.valueOf(time));
		st.setTime(5, Time.valueOf(time));
		st.setTime(6, Time.valueOf(Time.valueOf(time).toLocalTime().plusMinutes(length)));
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {

			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			Seminar seminar;
			
			if(lazy) {
				seminar = new SeminarProxy();
			}
			else {
				seminar = new Seminar();
				seminar.setBookers(activity.getBookers());
			}
			
			seminar.setId(activity.getId());
			seminar.setTime(activity.getTime());
			seminar.setLength(activity.getLength());
			seminar.setDescription(activity.getDescription());
			seminar.setManager(activity.getManager());
			seminar.setClassroom(activity.getClassroom());
			seminar.setDate(rs.getDate("seminar_date").toString());

			seminars.add(seminar);
		}
		return seminars;
	}
	
	@Override
	public List<Seminar> findNotExpired(boolean lazy) throws SQLException {

		List<Seminar> seminars = new ArrayList<>();

		String query = "select * from seminars S, activities A where S.id = A.id and (seminar_date > current_date or (seminar_date = current_date and activity_time > current_time))";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);
		
		while (rs.next()) {

			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			Seminar seminar;
			
			if(lazy) {
				seminar = new SeminarProxy();
			}
			else {
				seminar = new Seminar();
				seminar.setBookers(activity.getBookers());
			}
			
			seminar.setId(activity.getId());
			seminar.setTime(activity.getTime());
			seminar.setLength(activity.getLength());
			seminar.setDescription(activity.getDescription());
			seminar.setManager(activity.getManager());
			seminar.setClassroom(activity.getClassroom());
			seminar.setDate(rs.getDate("seminar_date").toString());

			seminars.add(seminar);
		}
		return seminars;
	}
	
	@Override
	public List<Seminar> findBookedByStudent(String studentCf, boolean lazy, int amount, int offset) throws SQLException {

		List<Seminar> seminars = new ArrayList<>();

		String query = "select * from seminars S, books B where S.id = B.activity and B.student = ? order by seminar_date desc limit ? offset ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, studentCf);
		st.setInt(2, amount);
		st.setInt(3, offset);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			Seminar seminar;
			
			if(lazy) {
				seminar = new SeminarProxy();
			}
			else {
				seminar = new Seminar();
				seminar.setBookers(activity.getBookers());
			}
			
			seminar.setId(activity.getId());
			seminar.setTime(activity.getTime());
			seminar.setLength(activity.getLength());
			seminar.setDescription(activity.getDescription());
			seminar.setManager(activity.getManager());
			seminar.setClassroom(activity.getClassroom());
			seminar.setDate(rs.getDate("seminar_date").toString());

			seminars.add(seminar);
		}
		return seminars;
	}
	
	@Override
	public List<Seminar> findBookedByStudentNotExpired(String studentCf, boolean lazy) throws SQLException {

		List<Seminar> seminars = new ArrayList<>();

		String query = "select * from activities A, seminars S, books B where S.id = A.id and A.id = B.activity and B.student = ? and (seminar_date > current_date or (seminar_date = current_date and activity_time > current_time))";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, studentCf);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Activity activity = DatabaseManager.getInstance().getActivityDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			Seminar seminar;
			
			if(lazy) {
				seminar = new SeminarProxy();
			}
			else {
				seminar = new Seminar();
				seminar.setBookers(activity.getBookers());
			}
			
			seminar.setId(activity.getId());
			seminar.setTime(activity.getTime());
			seminar.setLength(activity.getLength());
			seminar.setDescription(activity.getDescription());
			seminar.setManager(activity.getManager());
			seminar.setClassroom(activity.getClassroom());
			seminar.setDate(rs.getDate("seminar_date").toString());

			seminars.add(seminar);
		}
		return seminars;
	}

	@Override
	public Activity findByDateTimeClassroomProfessor(String cfProfessor, Long idClassroom) throws SQLException {
		Activity activity = null;

		String firstQuery = "select a.activity_length from activities as a, seminars as s where s. id = a.id";

		PreparedStatement s = conn.prepareStatement(firstQuery);
		ResultSet result = s.executeQuery();
		while(result.next()) {

			String secondQuery = "select a.id, a.activity_time, a.activity_length, a.description, a.professor, a.classroom " +
					"from activities as a, seminars as s where s. id = a.id and s.seminar_date = ? and" +
					"classroom = ? and professor = ? and ((activity_time <= ? and ? < activity_time + interval '1 min' * activity_length) " +
					"or (? <= activity_time and activity_time < ?))";

			PreparedStatement st = conn.prepareStatement(secondQuery);

			st.setDate(1, Date.valueOf(LocalDate.now()));
			st.setLong(2, idClassroom);
			st.setString(3, cfProfessor);
			st.setTime(4, Time.valueOf(LocalTime.now()));
			st.setTime(5, Time.valueOf(LocalTime.now()));
			st.setTime(6, Time.valueOf(LocalTime.now()));
			st.setTime(6, Time.valueOf(LocalTime.now().plusMinutes(result.getInt(1))));


			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				activity = new Activity();
				activity.setId(rs.getLong("id"));
				activity.setTime(rs.getTime("activity_time").toString());
				activity.setLength(rs.getInt("activity_length"));
				activity.setDescription(rs.getString("description"));
				activity.setManager(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString("professor")));
				activity.setClassroom(DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong("classroom")));
				activity.setBookers(DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong("id"), true));
			}
		}
		return activity;
	}
}
