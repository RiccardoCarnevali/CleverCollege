package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.CheckInCheckOut;
import com.clevercollege.persistence.DatabaseManager;

public class CheckInCheckOutDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<CheckInCheckOut> checkInCheckOutsFromDb = null;
		
		try {
			checkInCheckOutsFromDb = DatabaseManager.getInstance().getCheckInCheckOutDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<CheckInCheckOut> checkInCheckOuts = new ArrayList<>();
		checkInCheckOuts.add(new CheckInCheckOut(2, null, null, null, null));
		
		assertArrayEquals(checkInCheckOuts.toArray(), checkInCheckOutsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		CheckInCheckOut checkInCheckOutFromDb1 = null;
		CheckInCheckOut checkInCheckOutFromDb2 = null;
		try {
			checkInCheckOutFromDb1 = DatabaseManager.getInstance().getCheckInCheckOutDao().findByPrimaryKey(2);
			checkInCheckOutFromDb2 = DatabaseManager.getInstance().getCheckInCheckOutDao().findByPrimaryKey(1);		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		CheckInCheckOut checkInCheckOut = new CheckInCheckOut(2, null, null, null, null);
		
		assertEquals(checkInCheckOut, checkInCheckOutFromDb1);
		assertNull(checkInCheckOutFromDb2);
	}
	
}
