package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Course;
import com.clevercollege.model.Location;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LoadDataController {

	@PostMapping("/loadUsers")
	public List<User> loadUsers(String type, String sortBy, String like, int offset) {
		List<User> users = null;

		try {
			if (type.equals("users")) {
				users = DatabaseManager.getInstance().getUserDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			} else if (type.equals("students")) {
				users = DatabaseManager.getInstance().getStudentDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			} else if (type.equals("professors")) {
				users = DatabaseManager.getInstance().getProfessorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			} else {
				users = DatabaseManager.getInstance().getAdministratorDao().findByLike(sortBy, "%" + like + "%", 7,
						offset);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	@PostMapping("/loadCourses")
	public List<Course> loadCourses(String like, String type, Integer offset, HttpServletRequest req) {

		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute("user_type");
		
		if (user_type == null || (!user_type.equals("admin") && !user_type.equals("student")) || like == null
				|| offset == null)
			return null;

		List<Course> courses = null;

		try {
			if (type.equals("all"))
				courses = DatabaseManager.getInstance().getCourseDao().findByLike("%" + like + "%", 16, offset);
			else if (type.equals("followed")) {

				if (!user_type.equals("student")) 
					return null;
				
				Student student = (Student) session.getAttribute("user");

				if (student == null)
					return null;

				List<Course> followedCourses = student.getFollowedCourses();

				courses = new ArrayList<>();

				for (int i = 0; i < followedCourses.size(); ++i) {
					String professorName = followedCourses.get(i).getLecturer().getFirstName() + " "
							+ followedCourses.get(i).getLecturer().getLastName();
					if (followedCourses.get(i).getName().contains(like) || professorName.contains(like))
						courses.add(followedCourses.get(i));
				}
				
				if(offset < courses.size())
					courses = courses.subList(offset, (offset + 16) > courses.size() ? courses.size() : (offset + 16));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courses;
	}

	@PostMapping("/checkFollowed")
	public boolean[] checkFollowed(@RequestBody Course[] courses, HttpServletRequest req) {

		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute(("user_type"));

		if (user_type == null || !user_type.equals("student") || courses == null)
			return null;

		Student student = (Student) session.getAttribute("user");

		if (student == null)
			return null;

		boolean[] followed = new boolean[courses.length];

		for (int i = 0; i < courses.length; ++i) {
			if (student.getFollowedCourses().contains(courses[i]))
				followed[i] = true;
			else
				followed[i] = false;
		}

		return followed;
	}

	@PostMapping("/loadLocations")
	public List<Location> loadLocations(String type, String like, int offset) {
		List<Location> locations = null;

		try {
			if (type.equals("locations"))
				locations = DatabaseManager.getInstance().getLocationDao().findByLike("%" + like + "%", 16, offset);
			else
				locations = DatabaseManager.getInstance().getClassroomDao().findByLike("%" + like + "%", 16, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return locations;
	}
	
	@PostMapping("/loadLessonsForCourse")
	public List<SingleLesson> loadLessonsForCourse(Long courseId, HttpServletRequest req) {
		
		List<SingleLesson> singleLessons = null;
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("student") || courseId == null)
			return null;
		
		try {
			singleLessons = DatabaseManager.getInstance().getSingleLessonDao().findByCourseNotExpired(courseId, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return singleLessons;
	}
	
	@PostMapping("/loadBookedLessons")
	public List<SingleLesson> loadBookedLessons(HttpServletRequest req) {
		
		List<SingleLesson> singleLessons = null;
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("student"))
			return null;
		
		Student student = (Student) session.getAttribute("user");
		
		if(student == null)
			return null;
		
		try {
			return DatabaseManager.getInstance().getSingleLessonDao().findBookedByStudentNotExpired(student.getCf(), true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return singleLessons;
	}
}
