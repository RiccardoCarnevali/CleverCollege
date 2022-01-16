package com.clevercollege.controller;

import java.io.File;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class RemoveDataController {

	@PostMapping("/removeUser")
	public String removeUser(String cf, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || cf == null)
			return "error";
		
		User user = (User) session.getAttribute("user");
		if(user == null)
			return "error";
		
		if(user.getCf().equals(cf))
			return "remove self";
		
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
		
		if(user_type == null || !user_type.equals("admin") || professor == null)
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
	public String removeCourse(Long id, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || id == null)
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
	public String checkClassroomsActivities(Long classroom, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || classroom == null)
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
	public String removeLocation(Long id, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || id == null)
			return "error";
		
		try {
			DatabaseManager.getInstance().getLocationDao().delete(id);
			DatabaseManager.getInstance().commit();
			File qrCode = new File("src/main/resources/static/assets/images/locations-qr-codes/location_" + id + ".png");
			File qrCodeBin = new File("target/classes/static/assets/images/locations-qr-codes/location_" + id + ".png");
			qrCode.delete();
			qrCodeBin.delete();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		
		return "ok";
	}
	
}
