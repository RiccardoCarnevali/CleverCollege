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
	@GetMapping("/view_activities")
	public List<Activity> viewActivities(HttpSession session) {
		String userType = (String) session.getAttribute("user_type");
		String cf = (String) session.getAttribute("user_cf");
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

	@GetMapping("/view_single_lessons")
	public List<SingleLesson> viewSingleLessons(HttpSession session) {
		String userType = (String) session.getAttribute("user_type");
		String cf = (String) session.getAttribute("user_cf");
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

	@GetMapping("/view_weekly_lessons")
	public List<WeeklyLesson> viewWeeklyLessons(HttpSession session) {
		String userType = (String) session.getAttribute("user_type");
		String cf = (String) session.getAttribute("user_cf");
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

	@GetMapping("/view_seminars")
	public List<Seminar> viewSeminars(HttpSession session) {
		String userType = (String) session.getAttribute("user_type");
		String cf = (String) session.getAttribute("user_cf");
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

	@PostMapping("/enable_weekly_lesson")
	public void enableWeeklyLesson(Long id, boolean disable, boolean indefinite) {
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

	@PostMapping("/create_activity")
	public void createActivity(HttpSession session, HttpServletRequest request, String jsonString, String type) {
		if(jsonString == null || type == null)
			return;
		try {
			ObjectMapper mapper = new ObjectMapper();
			if(request.getSession().getAttribute("user_type") == null || request.getSession().getAttribute("user") == null) {
				return;
			}
			if (!request.getSession().getAttribute("user_type").equals("professor")) {
				return;
			}
			User user = (User) request.getSession().getAttribute("user");
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

	@PostMapping("/delete_activity")
	public void deleteActivity(Long id) {
		try {
			DatabaseManager.getInstance().getActivityDao().delete(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
