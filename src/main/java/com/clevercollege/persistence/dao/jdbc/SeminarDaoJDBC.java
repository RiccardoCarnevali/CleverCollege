package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		List<Seminar> activities = new ArrayList<>();

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
						DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong("id")));
			}
			seminar.setId(rs.getLong("id"));
			seminar.setTime(rs.getTime("activity_time").toString());
			seminar.setDate(rs.getDate("seminar_date").toString());
			seminar.setLength(rs.getInt("activity_length"));
			seminar.setDescription(rs.getString("description"));
			seminar.setManager(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(cf));
			seminar.setClassroom(
					DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong("classroom")));

			activities.add(seminar);
		}

		return activities;
	}

}
