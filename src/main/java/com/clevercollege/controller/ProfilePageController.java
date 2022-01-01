package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@Controller
public class ProfilePageController {

	@GetMapping("/myprofile")
	public String getProfilePage() {
		return "myprofile";
	}
	
	@PostMapping("/updateDescription")
	public void updateDescription(String description, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if (session == null) return; 
			User u = (User) session.getAttribute("user");
			if(u != null) {
				u.setDescription(description);
				DatabaseManager.getInstance().getUserDao().saveOrUpdate(u);				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
