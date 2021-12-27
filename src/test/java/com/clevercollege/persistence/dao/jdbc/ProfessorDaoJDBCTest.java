package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

public class ProfessorDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<User> professorsFromDb = null;
		
		try {
			professorsFromDb = DatabaseManager.getInstance().getProfessorDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<User> professors = new ArrayList<>();
		professors.add(new User("aaaaaaaaaaaaaaaa", null, null, null, null, null, null));
		
		assertArrayEquals(professors.toArray(), professorsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		User professorFromDb1 = null;
		User professorFromDb2 = null;
		try {
			professorFromDb1 = DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey("aaaaaaaaaaaaaaaa");
			professorFromDb2 = DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey("aaaaaaaaaaaaaaak");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		User professor = new User("aaaaaaaaaaaaaaaa", null, null, null, null, null, null);
				
		assertEquals(professor, professorFromDb1);
		assertNull(professorFromDb2);
	}
}
