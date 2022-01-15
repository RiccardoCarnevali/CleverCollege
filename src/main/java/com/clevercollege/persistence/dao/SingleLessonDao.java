package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.SingleLesson;

public interface SingleLessonDao {
	
	public List<SingleLesson> findAll(boolean lazy) throws SQLException;

	public SingleLesson findByPrimaryKey(long id, boolean lazy) throws SQLException;

	public void saveOrUpdate(SingleLesson singleLesson) throws SQLException;

	public void delete(long id) throws SQLException;

	public List<SingleLesson> findByProfessor(String cf, boolean lazy) throws SQLException;
	
	public List<SingleLesson> findByCourseNotExpired(long courseId, boolean lazy) throws SQLException;
	
	public List<SingleLesson> findBookedByStudent(String studentCf, boolean lazy, int amount, int offset) throws SQLException;
	
	public List<SingleLesson> findBookedByStudentNotExpired(String studentCf, boolean lazy) throws SQLException;
	
	public List<SingleLesson> findBookedByStudentThisWeek(String studentCf, boolean lazy) throws SQLException;
	
	public List<SingleLesson> findByCollidingTimeForStudent(String date, String time, int length, String studentCf, boolean lazy) throws SQLException;
}
