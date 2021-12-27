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

public class ClassroomDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<Location> classroomsFromDb = null;
		
		try {
			classroomsFromDb = DatabaseManager.getInstance().getClassroomDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Location> classrooms = new ArrayList<>();
		classrooms.add(new Location(3, null, 0));
		
		assertArrayEquals(classrooms.toArray(), classroomsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		Location classroomFromDb1 = null;
		Location classroomFromDb2 = null;
		try {
			classroomFromDb1 = DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(3);
			classroomFromDb2 = DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Location classroom = new Location(3, null, 0);
		
		assertEquals(classroom, classroomFromDb1);
		assertNull(classroomFromDb2);
	}
}
