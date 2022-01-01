package com.clevercollege.controller;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.persistence.DatabaseManager;

@RestController
public class RemoveDataController {

	@PostMapping("/removeUser")
	public String removeUser(String cf) {
		try {
			DatabaseManager.getInstance().getUserDao().delete(cf);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		};
		
		return "ok";
	}
	
	@PostMapping("/checkProfessorsCourses")
	public String checkProfessorsCourses(String professor) {
		try {
			if(DatabaseManager.getInstance().getCourseDao().findByProfessor(professor).size() != 0)
				return "yes";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "no";
	}
	
	@PostMapping("/removeCourse")
	public String removeCourse(long id) {
		try {
			DatabaseManager.getInstance().getCourseDao().delete(id);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		
		return "ok";
	}
	
	@PostMapping("/checkClassroomsActivities")
	public String checkClassroomsActivities(long classroom) {
		try {
			if(DatabaseManager.getInstance().getActivityDao().findByClassroom(classroom, true).size() != 0)
				return "yes";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "no";
	}
	
	@PostMapping("/removeLocation")
	public String removeLocation(long id) {
		try {
			DatabaseManager.getInstance().getLocationDao().delete(id);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		
		return "ok";
	}
	
}
