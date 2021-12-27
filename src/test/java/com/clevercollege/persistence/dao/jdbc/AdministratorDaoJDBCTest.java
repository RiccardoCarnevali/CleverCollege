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

public class AdministratorDaoJDBCTest {
	
	@Test
	public void findAllWorks() {
		List<User> administratorsFromDb = null;
		
		try {
			administratorsFromDb = DatabaseManager.getInstance().getAdministratorDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<User> administrators = new ArrayList<>();
		administrators.add(new User("aaaaaaaaaaaaaaac", null, null, null, null, null, null));
		
		assertArrayEquals(administrators.toArray(), administratorsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		User administratorFromDb1 = null;
		User administratorFromDb2 = null;
		try {
			administratorFromDb1 = DatabaseManager.getInstance().getAdministratorDao().findByPrimaryKey("aaaaaaaaaaaaaaac");
			administratorFromDb2 = DatabaseManager.getInstance().getAdministratorDao().findByPrimaryKey("osnavoangvoaik");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		User administrator = new User("aaaaaaaaaaaaaaac", null, null, null, null, null, null);
		
		assertEquals(administrator, administratorFromDb1);
		assertNull(administratorFromDb2);
	}
}
