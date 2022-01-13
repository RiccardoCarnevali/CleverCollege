package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Location;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LocationController {
	@GetMapping("/getLocations")
	public List<Location> getLocations() {
		List<Location> locations = new ArrayList<Location>();
		try {
			locations = DatabaseManager.getInstance().getLocationDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locations;
	}

	@PostMapping("/getLocations")
	public List<Location> getLocations(String like, Integer offset, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || like == null || offset == null)
			return null;
		
		List<Location> locations = null;
		
		try {
			locations = DatabaseManager.getInstance().getLocationDao().findByLike("%" + like + "%", 16, offset);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return locations;
	}
	
	@PostMapping("/getLocationById")
	public Location getLocationById(Long id) {
		Location location = null;
		try {
			location = DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}

	@GetMapping("/getClassrooms")
	public List<Location> getClassrooms() {
		List<Location> classrooms = new ArrayList<Location>();
		try {
			classrooms = DatabaseManager.getInstance().getClassroomDao().findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return classrooms;
	}

	@PostMapping("/getClassrooms")
	public List<Location> getClassrooms(String like, Integer offset, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || (!user_type.equals("professor") && !user_type.equals("admin")) 
				|| like == null || offset == null)
			return null;
		
		List<Location> classrooms = null;
		
		try {
			classrooms = DatabaseManager.getInstance().getClassroomDao().findByLike("%" + like + "%", 16, offset);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return classrooms;
	}
}
