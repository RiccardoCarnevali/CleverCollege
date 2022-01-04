package com.clevercollege.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class UpdateProfileController {
	
	@PostMapping("/updateDescription")
	public void updateDescription(String description, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if (session == null) return; 
			User u = (User) session.getAttribute("user");
			if(u != null) {
				u.setDescription(description);
				DatabaseManager.getInstance().getUserDao().saveOrUpdate(u);
				DatabaseManager.getInstance().commit();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	@PostMapping("/updateProfilePicture")
	public String updateImage(@RequestParam("image") MultipartFile img, HttpServletRequest request) {
		System.out.println("arrivato " + img.getOriginalFilename());
		User u = (User) request.getSession().getAttribute("user");
		if (!img.isEmpty()) {
			String imgName = img.getOriginalFilename();
			try {
				if (Files.probeContentType(Paths.get(imgName)) == "image" || img.getSize() > 1000000) {
					File f = new File("src/main/resources/static/assets/images/pp/" + u.getCf() + ".png");
					f.createNewFile();
					img.transferTo(f.getAbsoluteFile());
					u.setProfilePicture(u.getCf() + ".png");
					DatabaseManager.getInstance().getUserDao().saveOrUpdate(u);
					DatabaseManager.getInstance().commit();
					return "ok";
				}
				else {
					System.out.println("non valido");
					return "error";
				}
				
			} catch (IllegalStateException | IOException | SQLException e) {
				return "error";
			}
		}
		return "error";
}
				
	@PostMapping(path="/putProfilePicture", produces=org.springframework.http.MediaType.IMAGE_PNG_VALUE)
	public ByteArrayResource putImage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		ByteArrayResource inputStream = null;
			try {
				if (u.getProfilePicture() != null) {
					inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get("src/main/resources/static/assets/images/pp/" + u.getProfilePicture())));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return inputStream;
	}
}
