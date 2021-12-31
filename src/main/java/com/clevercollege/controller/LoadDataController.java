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
	public List<User> loadUsers(String type, String sortBy, String like, int offset) {
		List<User> users = null;

		try {
			if(type.equals("users")) {
				users = DatabaseManager.getInstance().getUserDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
			else if(type.equals("students")) {
				users = DatabaseManager.getInstance().getStudentDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
			else if(type.equals("professors")) {
				users = DatabaseManager.getInstance().getProfessorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
			else {
				users = DatabaseManager.getInstance().getAdministratorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}

}
