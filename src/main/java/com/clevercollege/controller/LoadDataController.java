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
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class LoadDataController {

	@PostMapping("/loadUsers")
	public List<User> loadUsers(String type, String sortBy, String like, Integer offset, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || type == null || sortBy == null || like == null || offset == null)
			return null;
		
		List<User> users = null;
		
		if(!sortBy.equals("cf") && !sortBy.equals("first_name") && !sortBy.equals("last_name"))
			return new ArrayList<>();
		
		try {
			if (type.equals("users")) {
				users = DatabaseManager.getInstance().getUserDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			} else if (type.equals("students")) {
				users = DatabaseManager.getInstance().getStudentDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			} else if (type.equals("professors")) {
				users = DatabaseManager.getInstance().getProfessorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			}
			else if(type.equals("administrators")) {
				users = DatabaseManager.getInstance().getAdministratorDao().findByLike(sortBy, "%" + like + "%", 7, offset);
			} else {
				users = new ArrayList<>();
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
		
		if (user_type == null || (!user_type.equals("admin") && !user_type.equals("student")) || like == null || type == null
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
	public List<Location> loadLocations(String type, String like, Integer offset, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("admin") || type == null || like == null || offset == null)
			return null;
		
		List<Location> locations = null;

		try {
			if (type.equals("locations"))
				locations = DatabaseManager.getInstance().getLocationDao().findByLike("%" + like + "%", 16, offset);
			else if(type.equals("classrooms"))
				locations = DatabaseManager.getInstance().getClassroomDao().findByLike("%" + like + "%", 16, offset);
			else
				locations = new ArrayList<>();
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
	public List<SingleLesson> loadBookedLessons(int offset, HttpServletRequest req) {
		
		List<SingleLesson> singleLessons = null;
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("student"))
			return null;
		
		Student student = (Student) session.getAttribute("user");
		
		if(student == null)
			return null;
		
		try {
			return DatabaseManager.getInstance().getSingleLessonDao().findBookedByStudent(student.getCf(), true, 16, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return singleLessons;	
	}
	
	@PostMapping("/loadBookedLessonsNotExpired")
	public List<SingleLesson> loadBookedLessonsNotExpired(HttpServletRequest req) {
		
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
	
	@PostMapping("/loadSeminars")
	public List<Seminar> loadSeminars(HttpServletRequest req) {
		
		List<Seminar> seminars = null;
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("student"))
			return null;
		
		try {
			seminars = DatabaseManager.getInstance().getSeminarDao().findNotExpired(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return seminars;
	}
	
	@PostMapping("/loadBookedSeminars")
	public List<Seminar> loadBookedSeminars(Integer offset, HttpServletRequest req) {
		
		List<Seminar> seminars = null;
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("student") || offset == null)
			return null;
		
		Student student = (Student) session.getAttribute("user");
		
		if(student == null)
			return null;
		
		try {
			return DatabaseManager.getInstance().getSeminarDao().findBookedByStudent(student.getCf(), true, 16, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return seminars;
	}
	
	@PostMapping("/loadBookedSeminarsNotExpired")
	public List<Seminar> loadBookedSeminarsNotExpired(HttpServletRequest req) {
		
		List<Seminar> seminars = null;
		
		HttpSession session = req.getSession();
		
		String user_type = (String) session.getAttribute("user_type");
		
		if(user_type == null || !user_type.equals("student"))
			return null;
		
		Student student = (Student) session.getAttribute("user");
		
		if(student == null)
			return null;
		
		try {
			return DatabaseManager.getInstance().getSeminarDao().findBookedByStudentNotExpired(student.getCf(), true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return seminars;
	}
}
