package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.SingleLesson;
import com.clevercollege.persistence.DatabaseManager;

public class SingleLessonDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<SingleLesson> singleLessonsFromDb = null;
		
		try {
			singleLessonsFromDb = DatabaseManager.getInstance().getSingleLessonDao().findAll(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<SingleLesson> singleLessons = new ArrayList<>();
		singleLessons.add(new SingleLesson(5, null, 0, null, null, null, null, null, null));
		
		assertArrayEquals(singleLessons.toArray(), singleLessonsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		SingleLesson singleLessonFromDb1 = null;
		SingleLesson singleLessonFromDb2 = null;
		try {
			singleLessonFromDb1 = DatabaseManager.getInstance().getSingleLessonDao().findByPrimaryKey(5, false);
			singleLessonFromDb2 = DatabaseManager.getInstance().getSingleLessonDao().findByPrimaryKey(6, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		SingleLesson singleLesson = new SingleLesson(5, null, 0, null, null, null, null, null, null);
				
		assertEquals(singleLesson, singleLessonFromDb1);
		assertNull(singleLessonFromDb2);
	}
}
