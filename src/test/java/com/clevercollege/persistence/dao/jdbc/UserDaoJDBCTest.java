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

public class UserDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<User> usersFromDb = null;
		
		try {
			usersFromDb = DatabaseManager.getInstance().getUserDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<User> users = new ArrayList<>();
		users.add(new User("aaaaaaaaaaaaaaaa", null, null, null, null, null, null));
		users.add(new User("aaaaaaaaaaaaaaab", null, null, null, null, null, null));
		users.add(new User("aaaaaaaaaaaaaaac", null, null, null, null, null, null));

		assertArrayEquals(users.toArray(), usersFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		User userFromDb1 = null;
		User userFromDb2 = null;
		try {
			userFromDb1 = DatabaseManager.getInstance().getUserDao().findByPrimaryKey("aaaaaaaaaaaaaaab");
			userFromDb2 = DatabaseManager.getInstance().getUserDao().findByPrimaryKey("aaaaaaaaaaaaaaad");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		User user = new User("aaaaaaaaaaaaaaab", null, null, null, null, null, null);
				
		assertEquals(user, userFromDb1);
		assertNull(userFromDb2);
	}
}
