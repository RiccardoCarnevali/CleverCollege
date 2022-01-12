package com.clevercollege.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.NotificationToken;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.services.NotificationService;

@RestController
public class NotificationController {
	
	@PostMapping("/set-new-client")
	public String setNewClient(HttpServletRequest req, String token) {
		
		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			return "error";
		}
		
		try {
			NotificationToken notToken = new NotificationToken(u.getCf(), token);
			if(DatabaseManager.getInstance().getNotificationTokenDao().findToken(notToken))
				return "already present";
			
			DatabaseManager.getInstance().getNotificationTokenDao().save(notToken);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		
		return "ok";
	}
	
	
	@PostMapping("/send-notification-to-all")
	public void sendNotificationToAll() {
		NotificationService.getInstance().schedule(new Runnable() {
			
			@Override
			public void run() {
				NotificationService.getInstance().sendNotificationToAll();
			}
		}, LocalDateTime.now(), "0");
	}
}
