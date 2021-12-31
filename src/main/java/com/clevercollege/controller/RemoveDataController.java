package com.clevercollege.controller;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.persistence.DatabaseManager;

@RestController
public class RemoveDataController {

	@PostMapping("/removeUser")
	public String removeUser(String cf) {
		try {
			DatabaseManager.getInstance().getUserDao().delete(cf);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		};
		
		return "ok";
	}
	
	@PostMapping("/removeCourse")
	public String removeCourse(long id) {
		try {
			DatabaseManager.getInstance().getCourseDao().delete(id);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		
		return "ok";
	}
	
}
