package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.persistence.DatabaseManager;

@RestController
public class RemoveDataController {

	@PostMapping("/removeUser")
	public String removeUser(String cf, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(!user_type.equals("admin") || cf == null)
			return "error";
		
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
	public String checkProfessorsCourses(String professor, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(!user_type.equals("admin") || professor == null)
			return "error";
		
		try {
			if(DatabaseManager.getInstance().getCourseDao().findByProfessor(professor).size() != 0)
				return "yes";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "no";
	}
	
	@PostMapping("/removeCourse")
	public String removeCourse(long id, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(!user_type.equals("admin"))
			return "error";
		
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
	public String checkClassroomsActivities(long classroom, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(!user_type.equals("admin"))
			return "error";
		
		try {
			if(DatabaseManager.getInstance().getActivityDao().findByClassroom(classroom, true).size() != 0)
				return "yes";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "no";
	}
	
	@PostMapping("/removeLocation")
	public String removeLocation(long id, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(!user_type.equals("admin"))
			return "error";
		
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
