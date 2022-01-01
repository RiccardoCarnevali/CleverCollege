package com.clevercollege.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewDataController {

	@GetMapping("/users")
	public String viewUsersPage() {
		return "view_users";
	}
	
	@GetMapping("/courses")
	public String viewCoursesPage() {
		return "view_courses";
	}
	
	@GetMapping("/locations")
	public String viewLocationsPage() {
		return "view_locations";
	}
}
