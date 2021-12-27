package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.Lesson;
import com.clevercollege.persistence.DatabaseManager;

public class LessonDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<Lesson> lessonsFromDb = null;
		
		try {
			lessonsFromDb = DatabaseManager.getInstance().getLessonDao().findAll(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(new Lesson(5, null, 0, null, null, null, null, null));
		lessons.add(new Lesson(6, null, 0, null, null, null, null, null));
		
		assertArrayEquals(lessons.toArray(), lessonsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		Lesson lessonFromDb1 = null;
		Lesson lessonFromDb2 = null;
		try {
			lessonFromDb1 = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(5, false);
			lessonFromDb2 = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(7, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Lesson lesson = new Lesson(5, null, 0, null, null, null, null, null);
				
		assertEquals(lesson, lessonFromDb1);
		assertNull(lessonFromDb2);
	}
}
