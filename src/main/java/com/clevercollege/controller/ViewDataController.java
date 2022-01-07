package com.clevercollege.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.clevercollege.model.User;

@Controller
public class ViewDataController {

	@GetMapping("/users")
	public String viewUsersPage(HttpServletRequest req) {
		
		HttpSession session = req.getSession();

		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/users");
			return "redirect:/login";
		}
		
		else {
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
		}
		
		return "view_users";
	}
	
	@GetMapping("/courses")
	public String viewCoursesPage(HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/courses");
			return "redirect:/login";
		}
		
		else {
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
		}
		
		return "view_courses";
	}
	
	@GetMapping("/locations")
	public String viewLocationsPage(HttpServletRequest req) {
		
		HttpSession session = req.getSession();

		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/locations");
			return "redirect:/login";
		}
		
		else {
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
		}
		
		return "view_locations";
	}
	
	@GetMapping("/activities/book-lessons")
	public String viewBookLessonsPage(HttpServletRequest req) {

		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/activities/book-lessons");
			return "redirect:/login";
		}
		else {
			if(!session.getAttribute("user_type").equals("student"))
				return "not_authorized";
		}
		
		return "book_lessons";
	}
	
	@GetMapping("/activities/book-seminars")
	public String viewBookSeminarsPage(HttpServletRequest req) {

		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/activities/book-seminars");
			return "redirect:/login";
		}
		else {
			if(!session.getAttribute("user_type").equals("student"))
				return "not_authorized";
		}
		
		return "book_seminars";
	}
	
	@GetMapping("/activities/my-bookings")
	public String viewMyBookingsPage(HttpServletRequest req) {

		HttpSession session = req.getSession();
		
		User u = (User) session.getAttribute("user");
		
		if(u == null) {
			session.setAttribute("after-login", "/activities/my-bookings");
			return "redirect:/login";
		}
		else {
			if(!session.getAttribute("user_type").equals("student"))
				return "not_authorized";
		}
		
		return "my_bookings";
	}
	
	@GetMapping("/insertOtherData")
    public String insertDataPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            session.setAttribute("after-login", "/insertOtherData");
            return "redirect:/login";
        }
        else {
            if(!session.getAttribute("user_type").equals("admin"))
                return "not_authorized";
        }
        return "insertOtherData";
    }


    @GetMapping("/insertNewUser")
    public String insertUserPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            session.setAttribute("after-login", "/insertNewUser");
            return "redirect:/login";
        }
        else {
            if(!session.getAttribute("user_type").equals("admin")) {
                return "not_authorized";
            }
        }
        return "insertNewUser";
    }
}
