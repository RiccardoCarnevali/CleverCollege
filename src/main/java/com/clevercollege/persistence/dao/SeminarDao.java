package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Seminar;

public interface SeminarDao {

	public List<Seminar> findAll(boolean lazy) throws SQLException;

	public Seminar findByPrimaryKey(long id, boolean lazy) throws SQLException;

	public void saveOrUpdate(Seminar seminar) throws SQLException;

	public void delete(long id) throws SQLException;

	public List<Seminar> findByProfessor(String cf, boolean lazy) throws SQLException;
	
	public List<Seminar> findByCollidingTimeForStudent(String date, String time, int length, String studentCf, boolean lazy) throws SQLException;

	public List<Seminar> findNotExpired(boolean lazy) throws SQLException;
	
	public List<Seminar> findBookedByStudent(String studentCf, boolean lazy, int amount, int offset) throws SQLException;
	
	public List<Seminar> findBookedByStudentNotExpired(String studentCf, boolean lazy) throws SQLException;

	public List<Seminar> findActiveByProfessor(String cf, boolean lazy) throws SQLException;
}
