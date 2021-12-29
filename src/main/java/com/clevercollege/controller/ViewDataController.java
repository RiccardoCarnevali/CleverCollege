package com.clevercollege.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewDataController {

	@GetMapping("/viewData")
	public String viewDataPage() {
		return "view_data";
	}

}
