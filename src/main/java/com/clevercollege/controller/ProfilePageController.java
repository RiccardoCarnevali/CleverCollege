package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@Controller
public class ProfilePageController {

	@GetMapping("/myprofile")
	public String getProfilePage() {
		return "myprofile";
	}
	
	@GetMapping("/updateDescription")
	public void updateDescription(String description, HttpSession session) {
		try {
			User u = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(session.getAttribute("cf").toString());
			if(description != null) {
				u.setDescription(description);
				DatabaseManager.getInstance().getUserDao().saveOrUpdate(u);				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
