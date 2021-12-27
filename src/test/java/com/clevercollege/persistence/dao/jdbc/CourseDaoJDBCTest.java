package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.Course;
import com.clevercollege.persistence.DatabaseManager;

public class CourseDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<Course> coursesFromDb = null;
		
		try {
			coursesFromDb = DatabaseManager.getInstance().getCourseDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(2, null, null));
		
		assertArrayEquals(courses.toArray(), coursesFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		Course courseFromDb1 = null;
		Course courseFromDb2 = null;
		try {
			courseFromDb1 = DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(2);
			courseFromDb2 = DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Course course= new Course(2, null, null);
				
		assertEquals(course, courseFromDb1);
		assertNull(courseFromDb2);
	}
}
