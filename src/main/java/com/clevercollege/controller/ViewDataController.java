package com.clevercollege.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.clevercollege.model.User;

@Controller
public class ViewDataController {

	@GetMapping("/users")
	public String viewUsersPage(HttpServletRequest req) {
		
		HttpSession session = req.getSession();

		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/users");
			return "redirect:/login";
		}
		
		else {
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
		}
		
		return "view_users";
	}
	
	@GetMapping("/courses")
	public String viewCoursesPage(HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/courses");
			return "redirect:/login";
		}
		
		else {
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
		}
		
		return "view_courses";
	}
	
	@GetMapping("/locations")
	public String viewLocationsPage(HttpServletRequest req) {
		
		HttpSession session = req.getSession();

		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/locations");
			return "redirect:/login";
		}
		
		else {
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
		}
		
		return "view_locations";
	}
}
