package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.Location;
import com.clevercollege.persistence.DatabaseManager;

public class LocationDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<Location> locationsFromDb = null;
		
		try {
			locationsFromDb = DatabaseManager.getInstance().getLocationDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Location> locations= new ArrayList<>();
		locations.add(new Location(3, null, 0));
		
		assertArrayEquals(locations.toArray(), locationsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		Location locationFromDb1 = null;
		Location locationFromDb2 = null;
		try {
			locationFromDb1 = DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(3);
			locationFromDb2 = DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Location location = new Location(3, null, 0);
				
		assertEquals(location, locationFromDb1);
		assertNull(locationFromDb2);
	}
}
