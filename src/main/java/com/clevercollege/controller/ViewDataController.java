package com.clevercollege.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewDataController {

	@GetMapping("/viewUsers")
	public String viewUsersPage() {
		return "view_users";
	}
	
	@GetMapping("/viewCourses")
	public String viewCoursesPage() {
		return "view_courses";
	}
}
