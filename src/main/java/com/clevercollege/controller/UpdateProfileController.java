package com.clevercollege.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clevercollege.model.Activity;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class UpdateProfileController {

	@PostMapping("/loadBookedWeekActivities")
	public List<Activity> loadBookedWeekActivities(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("user");
		
		if (currentUser == null) return null;
		
		String uType = (String) session.getAttribute("user_type");
		if (uType == null) return null;				

		List<Activity> activities = new ArrayList<Activity>();
		List<SingleLesson> sl = null;
		List<Seminar> s = null;
		
		if (uType.equals("student")) {
			try {
				sl = DatabaseManager.getInstance().getSingleLessonDao().findBookedByStudentThisWeek(currentUser.getCf(), true);
				s = DatabaseManager.getInstance().getSeminarDao().findBookedByStudentThisWeek(currentUser.getCf(), true);
				
				if (!sl.isEmpty()) activities.addAll(sl);					
				if (!s.isEmpty()) activities.addAll(s);
				
				return activities;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}			
		}
		else if (uType.equals("professor")) {
			try {
				sl = DatabaseManager.getInstance().getSingleLessonDao().findByProfessorThisWeek(currentUser.getCf(), true);
				s = DatabaseManager.getInstance().getSeminarDao().findByProfessorThisWeek(currentUser.getCf(), true);
				
				activities.addAll(sl);					
				activities.addAll(s);
				
				return activities;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	@PostMapping("/updateDescription")
	public void updateDescription(String description, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if (session == null)
				return;
			User u = (User) session.getAttribute("user");
			if (u != null) {
				u.setDescription(description);
				DatabaseManager.getInstance().getUserDao().saveOrUpdate(u);
				DatabaseManager.getInstance().commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/updateProfilePicture")
	public String updateImage(@RequestParam("image") MultipartFile img, HttpServletRequest request) {
		User u = (User) request.getSession().getAttribute("user");
		
		if (img.isEmpty())
			return "error";
		
		String imgName = img.getOriginalFilename();
		try {
			if (img.getSize() > 1000000)
				return "img too big";
			
			if(!Files.probeContentType(Paths.get(imgName)).split("/")[0].equals("image"))
				return "not an img";
	
			File f = new File("src/main/resources/static/assets/images/pp/" + u.getCf() + ".png");
			f.createNewFile();
			img.transferTo(f.getAbsoluteFile());
			
			BufferedImage bImg = ImageIO.read(f);
			
			if(bImg.getWidth() < 180 || bImg.getHeight() < 180) {
				f.delete();
				return "img too small";
			}
			
			File fBin = new File("target/classes/static/assets/images/pp/" + u.getCf() + ".png");
			Files.copy(f.toPath(), fBin.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			u.setProfilePicture(u.getCf() + ".png");
			DatabaseManager.getInstance().getUserDao().saveOrUpdate(u);
			DatabaseManager.getInstance().commit();
			return "ok";

		} catch (IllegalStateException | IOException | SQLException e) {
			e.printStackTrace();
			return "error";
		}
	}

	@PostMapping(path = "/putProfilePicture", produces = org.springframework.http.MediaType.IMAGE_PNG_VALUE)
	public ByteArrayResource putImage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		ByteArrayResource inputStream = null;
		if (u.getProfilePicture() != null) {
			try {
					inputStream = new ByteArrayResource(Files.readAllBytes(
							Paths.get("src/main/resources/static/assets/images/pp/" + u.getProfilePicture())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return inputStream;
	}

	@PostMapping("/updatePassword")
	public String updatePassword(String oldPwd, String newPwd, String confirmPwd, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		if (u != null && newPwd != null && confirmPwd != null) {
			try {
				if (!oldPwd.isEmpty()) {
					if (!newPwd.isEmpty() && newPwd.equals(confirmPwd)) {
						if(BCrypt.checkpw(oldPwd, u.getPassword())) {
							u.setPassword(BCrypt.hashpw(newPwd, BCrypt.gensalt(12)));
							DatabaseManager.getInstance().getUserDao().saveOrUpdate(u);
							DatabaseManager.getInstance().commit();
							return "correct";
						}
						else {
							return "wrong password";
						}
					}
					else {
						return "new pwd not matching";
					}				
				}
				else {
					return "pwd missing";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		return "error";
	}
}