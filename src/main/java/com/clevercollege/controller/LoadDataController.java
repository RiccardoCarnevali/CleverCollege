package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LoadDataController {

	@GetMapping("/loadUsers")
	public List<User> loadUsers() {
		List<User> users = null;
		
		try {
			users = DatabaseManager.getInstance().getUserDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
}
