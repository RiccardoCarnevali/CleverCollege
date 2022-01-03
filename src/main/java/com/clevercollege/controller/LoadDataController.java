package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Course;
import com.clevercollege.model.Location;
import com.clevercollege.model.Student;
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
	public List<Course> loadCourses(String like, String type, int offset, HttpServletRequest req) {
		List<Course> courses = null;
		
		try {
			if(type.equals("all"))
				courses = DatabaseManager.getInstance().getCourseDao().findByLike("%" + like + "%", 16, offset);
			else {
				HttpSession session = req.getSession();
				
				Student student = (Student) session.getAttribute("user");
				List<Course> followedCourses = student.getFollowedCourses();
				
				courses = new ArrayList<>();
				
				for(int i = 0; i < followedCourses.size(); ++i) {
					String professorName = followedCourses.get(i).getLecturer().getFirstName() + " " + followedCourses.get(i).getLecturer().getLastName();
					if(followedCourses.get(i).getName().contains(like) || professorName.contains(like))
						courses.add(followedCourses.get(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courses;
	}
	
	@PostMapping("/checkFollowed")
	public boolean[] checkFollowed(@RequestBody Course[] courses, HttpServletRequest req)  {

		boolean[] followed = new boolean[courses.length];
		
		HttpSession session = req.getSession();
		
		Student student = (Student) session.getAttribute("user");
		
		for(int i = 0; i < courses.length; ++i) {
			if(student.getFollowedCourses().contains(courses[i]))
				followed[i] = true;
			else
				followed[i] = false;
		}
		
		return followed;
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
