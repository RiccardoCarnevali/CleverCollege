package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Location;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LocationController {
	@GetMapping("/get_locations") 
	public List<Location> getLocations(){
		List<Location> locations = new ArrayList<Location>();
		try {
			locations = DatabaseManager.getInstance().getLocationDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locations;
	}
	
	@GetMapping("/get_classrooms") 
	public List<Location> getClassrooms(){
		List<Location> classrooms = new ArrayList<Location>();
		try {
			classrooms = DatabaseManager.getInstance().getClassroomDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return classrooms;
	}
}
