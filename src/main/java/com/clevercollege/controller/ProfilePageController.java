package com.clevercollege.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@Controller
public class ProfilePageController {

	@GetMapping("/myprofile")
	public String getProfilePage(HttpServletRequest request) {
		//test
//		try {
//            User u = DatabaseManager.getInstance().getUserDao().findByPrimaryKey("aaaaaaaaaaaaaaac");
//            if(u != null) {
//            	HttpSession session = request.getSession(true);
//                session.setAttribute("user", u);
//                return "myprofile";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
		//endtest
		return "myprofile";
	}
}
