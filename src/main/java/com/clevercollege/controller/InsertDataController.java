package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Student;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class InsertDataController {

	@PostMapping("/setFollowedCourse")
	public String setFollowedCourse(Long courseId, Boolean followed, HttpServletRequest req) {

		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("student") || courseId == null || followed == null)
			return "error";
		
		Student student = (Student) session.getAttribute("user");
		
		if(student == null)
			return "error";
		
		try {
			if(followed) {
				DatabaseManager.getInstance().getStudentDao().followCourseForStudent(courseId, student.getCf());
				student.getFollowedCourses().add(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(courseId));
			}
			else {
				DatabaseManager.getInstance().getStudentDao().unfollowCourseForStudent(courseId, student.getCf());
				student.getFollowedCourses().remove(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(courseId));
			}
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			return "error";
		}
		
		return "ok";
	}
	
}
