package com.clevercollege.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewProfileController {

	@GetMapping("/myprofile")
	public String getProfilePage() {
		return "myprofile";
	}
}
