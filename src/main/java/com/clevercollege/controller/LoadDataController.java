package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Course;
import com.clevercollege.model.Location;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LoadDataController {
	
	@PostMapping("/loadUsers")
	public List<User> loadUsers(String type, String sortBy, String like, int offset) {
		List<User> users = null;

		try {
			if(type.equals("users")) {
				users = DatabaseManager.getInstance().getUserDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
			else if(type.equals("students")) {
				users = DatabaseManager.getInstance().getStudentDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
			else if(type.equals("professors")) {
				users = DatabaseManager.getInstance().getProfessorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
			else {
				users = DatabaseManager.getInstance().getAdministratorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	@PostMapping("/loadCourses")
	public List<Course> loadCourses(String like, int offset) {
		List<Course> courses = null;
		
		try {
			courses = DatabaseManager.getInstance().getCourseDao().findByLike("%" + like + "%", 16, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courses;
	}
	
	@PostMapping("/loadLocations")
	public List<Location> loadLocations(String type, String like, int offset) {
		List<Location> locations = null;
		
		try {
			if(type.equals("locations"))
				locations = DatabaseManager.getInstance().getLocationDao().findByLike("%" + like + "%", 16, offset);
			else
				locations = DatabaseManager.getInstance().getClassroomDao().findByLike("%" + like + "%", 16, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return locations;
	}
}
