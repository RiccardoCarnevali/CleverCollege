package com.clevercollege.persistence.dao.jdbc;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.clevercollege.model.WeeklyLesson;
import com.clevercollege.persistence.DatabaseManager;

public class WeeklyLessonDaoJDBCTest {

	@Test
	public void findAllWorks() {
		List<WeeklyLesson> weeklyLessonsFromDb = null;
		
		try {
			weeklyLessonsFromDb = DatabaseManager.getInstance().getWeeklyLessonDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<WeeklyLesson> weeklyLessons = new ArrayList<>();
		weeklyLessons.add(new WeeklyLesson(6, null, 0, null, null, null, null, 0, false, false));

		assertArrayEquals(weeklyLessons.toArray(), weeklyLessonsFromDb.toArray());
	}
	
	@Test
	public void findByPrimaryKeyWorks() {
		WeeklyLesson weeklyLessonFromDb1 = null;
		WeeklyLesson weeklyLessonFromDb2 = null;
		try {
			weeklyLessonFromDb1 = DatabaseManager.getInstance().getWeeklyLessonDao().findByPrimaryKey(6);
			weeklyLessonFromDb2 = DatabaseManager.getInstance().getWeeklyLessonDao().findByPrimaryKey(7);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		WeeklyLesson weeklyLesson= new WeeklyLesson(6, null, 0, null, null, null, null, 0, false, false);
				
		assertEquals(weeklyLesson, weeklyLessonFromDb1);
		assertNull(weeklyLessonFromDb2);
	}
}
