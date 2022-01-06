package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Activity;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.User;
import com.clevercollege.model.WeeklyLesson;
import com.clevercollege.persistence.DatabaseManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class HandleActivitiesController {
	@GetMapping("/getActivities")
	public List<Activity> getActivities(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user_type") == null || session.getAttribute("user") == null) {
			return null;
		}
		String userType = (String) session.getAttribute("user_type");
		String cf = ((User) session.getAttribute("user")).getCf();
		List<Activity> activities = new ArrayList<Activity>();
		if (userType == "professor")
			try {
				activities = DatabaseManager.getInstance().getActivityDao().findByProfessor(cf, true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else {
			// TODO: Handle user activities
		}
		return activities;
	}

	@GetMapping("/getSingleLessons")
	public List<SingleLesson> getSingleLessons(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user_type") == null || session.getAttribute("user") == null) {
			return null;
		}
		String userType = (String) session.getAttribute("user_type");
		String cf = ((User) session.getAttribute("user")).getCf();
		List<SingleLesson> singleLessons = new ArrayList<SingleLesson>();
		if (userType == "professor")
			try {
				singleLessons.addAll(DatabaseManager.getInstance().getSingleLessonDao().findByProfessor(cf, true));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else {
			// TODO: Handle user activities
		}
		return singleLessons;
	}

	@GetMapping("/getWeeklyLessons")
	public List<WeeklyLesson> getWeeklyLessons(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user_type") == null || session.getAttribute("user") == null) {
			return null;
		}
		String userType = (String) session.getAttribute("user_type");
		String cf = ((User) session.getAttribute("user")).getCf();
		List<WeeklyLesson> weeklyLessons = new ArrayList<WeeklyLesson>();
		if (userType == "professor")
			try {
				weeklyLessons.addAll(DatabaseManager.getInstance().getWeeklyLessonDao().findByProfessor(cf, true));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else {
			// TODO: Handle user activities
		}
		return weeklyLessons;
	}

	@GetMapping("/getSeminars")
	public List<Seminar> getSeminars(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user_type") == null || session.getAttribute("user") == null) {
			return null;
		}
		String userType = (String) session.getAttribute("user_type");
		String cf = ((User) session.getAttribute("user")).getCf();
		List<Seminar> seminars = new ArrayList<Seminar>();
		if (userType == "professor")
			try {
				seminars.addAll(DatabaseManager.getInstance().getSeminarDao().findByProfessor(cf, true));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else {
			// TODO: Handle user activities
		}
		return seminars;
	}

	@PostMapping("/enableWeeklyLesson")
	public void enableWeeklyLesson(HttpServletRequest request, Long id, boolean disable, boolean indefinite) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user_type") == null || !("professor").equals(session.getAttribute("user_type"))) {
			return;
		}
		try {	
			WeeklyLesson lesson = DatabaseManager.getInstance().getWeeklyLessonDao().findByPrimaryKey(id);
			lesson.setDisabled(disable);
			lesson.setDisabledIndefinitely(indefinite);
			DatabaseManager.getInstance().getWeeklyLessonDao().saveOrUpdate(lesson);
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/createActivity")
	public void createActivity(HttpServletRequest request, String jsonString, String type) {
		if (jsonString == null || type == null)
			return;
		HttpSession session = request.getSession();
		if (session.getAttribute("user_type") == null || session.getAttribute("user") == null
				|| !("professor").equals(session.getAttribute("user_type"))) {
			return;
		}
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			User user = (User) session.getAttribute("user");
			Long id = DatabaseManager.getInstance().getIdBroker().getNextActivityId();

			if (type.equals("single")) {
				SingleLesson single = mapper.readValue(jsonString, SingleLesson.class);
				single.setManager(user);
				single.setId(id);
				DatabaseManager.getInstance().getSingleLessonDao().saveOrUpdate(single);
			} else if (type.equals("weekly")) {
				WeeklyLesson weekly = mapper.readValue(jsonString, WeeklyLesson.class);
				weekly.setManager(user);
				weekly.setId(id);
				DatabaseManager.getInstance().getWeeklyLessonDao().saveOrUpdate(weekly);
			} else if (type.equals("seminar")) {
				Seminar seminar = mapper.readValue(jsonString, Seminar.class);
				seminar.setManager(user);
				seminar.setId(id);
				DatabaseManager.getInstance().getSeminarDao().saveOrUpdate(seminar);
			}

			DatabaseManager.getInstance().commit();
		} catch (SQLException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@PostMapping("/deleteActivity")
	public void deleteActivity(HttpServletRequest request, Long id) {
		HttpSession session = request.getSession();
		String userType = (String) session.getAttribute("user_type");
		if (userType == null || session.getAttribute("user") == null || 
				(!("professor").equals(userType) && !("admin").equals(userType))) {
			return;
		}
		try {
			DatabaseManager.getInstance().getActivityDao().delete(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
