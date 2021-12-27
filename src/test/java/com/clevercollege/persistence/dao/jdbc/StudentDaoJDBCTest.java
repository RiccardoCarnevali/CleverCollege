package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.Student;
import com.clevercollege.persistence.DatabaseManager;

public class StudentDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<Student> studentsFromDb = null;
		
		try {
			studentsFromDb = DatabaseManager.getInstance().getStudentDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Student> students = new ArrayList<>();
		students.add(new Student("aaaaaaaaaaaaaaab", null, null, null, null, null, null, null));
		
		assertArrayEquals(students.toArray(), studentsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		Student studentFromDb1 = null;
		Student studentFromDb2 = null;
		try {
			studentFromDb1 = DatabaseManager.getInstance().getStudentDao().findByPrimaryKey("aaaaaaaaaaaaaaab");
			studentFromDb2 = DatabaseManager.getInstance().getStudentDao().findByPrimaryKey("aaaaaaaaaaaaaaaa");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Student student = new Student("aaaaaaaaaaaaaaab", null, null, null, null, null, null, null);
				
		assertEquals(student, studentFromDb1);
		assertNull(studentFromDb2);
	}
	
	@Test
	public void findBookersForActivityWorks() {
		List<Student> bookersFromDb = null;
		 
		try {
			bookersFromDb = DatabaseManager.getInstance().getStudentDao().findBookersForActivity(5);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Student> bookers = new ArrayList<>();
		bookers.add(new Student("aaaaaaaaaaaaaaab", null, null, null, null, null, null, null));
		
		assertArrayEquals(bookers.toArray(), bookersFromDb.toArray());
	}
}
