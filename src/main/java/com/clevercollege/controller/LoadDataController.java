package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Course;
import com.clevercollege.model.Location;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LoadDataController {
	
	@PostMapping("/loadUsers")
	public List<User> loadUsers(String type, String sortBy, String like, int offset, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || type == null || sortBy == null || like == null)
			return null;
		
		List<User> users = null;
		
		if(!sortBy.equals("cf") && !sortBy.equals("first_name") && !sortBy.equals("last_name"))
			return new ArrayList<>();
		
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
			else if(type.equals("administrators")) {
				users = DatabaseManager.getInstance().getAdministratorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			} else {
				users = new ArrayList<>();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	@PostMapping("/loadCourses")
	public List<Course> loadCourses(String like, int offset, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || like == null)
			return null;
		
		List<Course> courses = null;
		
		try {
			courses = DatabaseManager.getInstance().getCourseDao().findByLike("%" + like + "%", 16, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courses;
	}
	
	@PostMapping("/loadLocations")
	public List<Location> loadLocations(String type, String like, int offset, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || type == null || like == null)
			return null;
		
		List<Location> locations = null;
		
		try {
			if(type.equals("locations"))
				locations = DatabaseManager.getInstance().getLocationDao().findByLike("%" + like + "%", 16, offset);
			else if(type.equals("classrooms"))
				locations = DatabaseManager.getInstance().getClassroomDao().findByLike("%" + like + "%", 16, offset);
			else
				locations = new ArrayList<>();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return locations;
	}
}
