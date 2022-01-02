package com.clevercollege.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterController {
	@GetMapping("/footer")
	public String loadFooter() {
		return "footer";
	}
}
