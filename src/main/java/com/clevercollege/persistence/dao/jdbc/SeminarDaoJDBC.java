package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
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
		
		String query = "select * from seminars";
		
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
			seminar.setDate(rs.getDate("seminar_date"));

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
			seminar.setDate(rs.getDate("seminar_date"));
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
			
			query = "update seminars set"
					+ "seminar_date = ?"
					+ "where id = ?";
			
			PreparedStatement updateSt = conn.prepareStatement(query);
			
			updateSt.setDate(1, seminar.getDate());
			updateSt.setLong(2, seminar.getId());
			
			updateSt.executeUpdate();
		}
		else {
			
			query = "insert into seminars values(?,?)";
			
			PreparedStatement insertSt = conn.prepareStatement(query);
			
			insertSt.setLong(1, seminar.getId());
			insertSt.setDate(2, seminar.getDate());
			
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

}
