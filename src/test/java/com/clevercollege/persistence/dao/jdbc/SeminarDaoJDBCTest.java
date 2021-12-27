package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.Seminar;
import com.clevercollege.persistence.DatabaseManager;

public class SeminarDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<Seminar> seminarsFromDb = null;
		
		try {
			seminarsFromDb = DatabaseManager.getInstance().getSeminarDao().findAll(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Seminar> seminars = new ArrayList<>();
		seminars.add(new Seminar(4, null, 0, null, null, null, null, null));
		
		assertArrayEquals(seminars.toArray(), seminarsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		Seminar seminarFromDb1 = null;
		Seminar seminarFromDb2 = null;
		try {
			seminarFromDb1 = DatabaseManager.getInstance().getSeminarDao().findByPrimaryKey(4, false);
			seminarFromDb2 = DatabaseManager.getInstance().getSeminarDao().findByPrimaryKey(1, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Seminar seminar = new Seminar(4, null, 0, null, null, null, null, null);
				
		assertEquals(seminar, seminarFromDb1);
		assertNull(seminarFromDb2);
	}
}
