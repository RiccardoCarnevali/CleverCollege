package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Seminar;

public interface SeminarDao {

	public List<Seminar> findAll(boolean lazy) throws SQLException;

	public Seminar findByPrimaryKey(long id, boolean lazy) throws SQLException;

	public void saveOrUpdate(Seminar seminar) throws SQLException;

	public void delete(long id) throws SQLException;

}
