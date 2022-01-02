package com.clevercollege.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActivitiesController {

	@GetMapping("/activities/handle_activities")
	public String handleActivities() {
		return "activities/handle_activities";
	}
}
