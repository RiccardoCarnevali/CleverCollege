package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.clevercollege.model.Course;
import com.clevercollege.model.Location;
import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

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
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
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
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
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
			String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
		}
		
		return "my_bookings";
	}
	
	@GetMapping("/courses/insert")
    public String insertCoursePage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            session.setAttribute("after-login", "/courses/insert");
            return "redirect:/login";
        }
        else {
        	String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
        }
        
        request.setAttribute("data_type", "course");
        return "insert_data";
    }
	
	@GetMapping("/locations/insert")
    public String insertLocationPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            session.setAttribute("after-login", "/locations/insert");
            return "redirect:/login";
        }
        else {
        	String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
        }
        request.setAttribute("data_type", "location");
        return "insert_data";
    }


    @GetMapping("/users/insert")
    public String insertUserPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            session.setAttribute("after-login", "/users/insert");
            return "redirect:/login";
        }
        else {
        	String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
        }
        return "insert_user";
    }
    
    @PostMapping("/users/edit")
    public String editUserPage(HttpServletRequest req, String userCf) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
                
        if(u == null) {
            session.setAttribute("after-login", "/users/edit");
            return "redirect:/login";
        }
        else {
        	String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
        }
        
        if(userCf == null)
        	return "error";
        
        try {
            Student student = DatabaseManager.getInstance().getStudentDao().findByPrimaryKey(userCf, true);
            
            if(student == null) {
            	User professor = DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(userCf);
            	
            	if(professor == null) {
            		User administrator = DatabaseManager.getInstance().getAdministratorDao().findByPrimaryKey(userCf);
            		
            		if(administrator == null)
            			return "error";
            		else {
            			req.setAttribute("user_to_edit", administrator);
            			req.setAttribute("type_to_edit", "administrator");
            		}
            	}
            	else {
            		req.setAttribute("user_to_edit", professor);
            		req.setAttribute("type_to_edit", "professor");
            	}
            }
            else {
            	req.setAttribute("user_to_edit", student);
            	req.setAttribute("type_to_edit", "student");
            }
		} catch (SQLException e) {
			return "error";
		}
        
        return "insert_user";
    }
    
    @PostMapping("/courses/edit")
    public String editCoursePage(HttpServletRequest req, Long courseId) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            session.setAttribute("after-login", "/courses/insert");
            return "redirect:/login";
        }
        else {
        	String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
        }
        if(courseId == null)
        	return "error";
        
        Course course = null;
        try {
			course = DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(courseId);
		} catch (SQLException e) {
			return "error";
		}
        
        if(course == null)
        	return "error";
        
        req.setAttribute("course_to_edit", course);
        return "insert_data";
    }
    
    @PostMapping("/locations/edit")
    public String editLocationPage(HttpServletRequest req, Long locationId) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            session.setAttribute("after-login", "/courses/insert");
            return "redirect:/login";
        }
        else {
        	String user_type = (String) session.getAttribute("user_type");
			if(user_type == null || !user_type.equals("admin"))
				return "not_authorized";
        }
        if(locationId == null)
        	return "error";
        
        Location location = null;
        try {
			location = DatabaseManager.getInstance().getLocationDao().findByPrimaryKey(locationId);
		} catch (SQLException e) {
			return "error";
		}
        
        if(location == null)
        	return "error";
        
        req.setAttribute("location_to_edit", location);
        return "insert_data";
    }
    
    @GetMapping("/check-in")
    public String getCheckInPage(HttpServletRequest request) {
    	User user = (User) request.getSession().getAttribute("user");
    	if(user == null) {
    		request.getSession().setAttribute("after-login", "check-in");
    		return "redirect:/login";
    	}
    	return "check-in";
    }
}
