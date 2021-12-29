package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LoadDataController {
	
	@GetMapping("/loadStudents")
	public List<Student> loadStudents() {
		List<Student> students = null;
		
		try {
			students = DatabaseManager.getInstance().getStudentDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return students;
	}
	
	@GetMapping("/loadProfessors")
	public List<User> loadProfessors() {
		List<User> professors = null;
		
		try {
			professors = DatabaseManager.getInstance().getProfessorDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return professors;
	}
	
	@GetMapping("/loadAdmins")
	public List<User> loadAdmins() {
		List<User> admins = null;
		
		try {
			admins = DatabaseManager.getInstance().getAdministratorDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return admins;
	}
}
