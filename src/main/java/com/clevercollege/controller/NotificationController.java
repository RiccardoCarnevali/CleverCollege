package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.NotificationToken;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class NotificationController {
	
	@PostMapping("/set-client")
	public String setNewClient(HttpServletRequest req, String token) {
		
		if(token == null)
			return "error";
		
		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			return "error";
		}
		
		try {
			NotificationToken notToken = new NotificationToken(u.getCf(), token, true);
			if(DatabaseManager.getInstance().getNotificationTokenDao().findToken(notToken))
				return "already present";
			
			DatabaseManager.getInstance().getNotificationTokenDao().saveOrUpdate(notToken);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		
		return "ok";
	}
	
	@PostMapping("/unset-client")
	public String unsetClient(HttpServletRequest req, String token) {
		
		if(token == null)
			return "error";
		
		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			return "error";
		}
		
		try {
			NotificationToken notToken = new NotificationToken(u.getCf(), token, false);

			DatabaseManager.getInstance().getNotificationTokenDao().saveOrUpdate(notToken);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		
		return "ok";
	}
	
	@PostMapping("/check-client")
	public String checkClient(HttpServletRequest req, String token) {
		if(token == null)
			return "error";
		
		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			return "not present";
		}
		
		try {
			NotificationToken notToken = new NotificationToken(u.getCf(), token, true);
			if(DatabaseManager.getInstance().getNotificationTokenDao().findToken(notToken))
				return "already present";
			else
				return "not present";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
	}
}
