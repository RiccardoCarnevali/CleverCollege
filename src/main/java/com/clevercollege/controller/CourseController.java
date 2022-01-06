package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Course;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class CourseController {
	@GetMapping("/getCourses") 
	public List<Course> getCourses(){
		List<Course> courses = new ArrayList<Course>();
		try {
			courses = DatabaseManager.getInstance().getCourseDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}
	
	@GetMapping("/getProfessorCourses")
	public List<Course> getProfessorCourses(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("user") == null || 
				!session.getAttribute("user_type").equals("professor"))
			return null;
		String professorCF = ((User)session.getAttribute("user")).getCf();
		List<Course> courses = null;
		try {
			courses = DatabaseManager.getInstance().getCourseDao().findByProfessor(professorCF);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courses;
	}
	
}
