package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Course;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class CourseController {
	@GetMapping("/get_courses") 
	public List<Course> getCourses(){
		List<Course> courses = new ArrayList<Course>();
		try {
			courses = DatabaseManager.getInstance().getCourseDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}
}
