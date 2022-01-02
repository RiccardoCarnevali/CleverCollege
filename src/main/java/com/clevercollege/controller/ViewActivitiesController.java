package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Activity;
import com.clevercollege.model.WeeklyLesson;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class ViewActivitiesController {
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

	@GetMapping("/view_single_activities")
	public List<Activity> viewSingleActivities(HttpSession session) {
		String userType = (String) session.getAttribute("user_type");
		String cf = (String) session.getAttribute("user_cf");
		List<Activity> activities = new ArrayList<Activity>();
		if (userType == "professor")
			try {
				activities.addAll(DatabaseManager.getInstance().getSeminarDao().findByProfessor(cf, true));
				activities.addAll(DatabaseManager.getInstance().getSingleLessonDao().findByProfessor(cf, true));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else {
			// TODO: Handle user activities
		}
		return activities;
	}

	@GetMapping("/view_weekly_activities")
	public List<WeeklyLesson> viewWeeklyActivities(HttpSession session) {
		String userType = (String) session.getAttribute("user_type");
		String cf = (String) session.getAttribute("user_cf");
		List<WeeklyLesson> activities = new ArrayList<WeeklyLesson>();
		if (userType == "professor")
			try {
				activities.addAll(DatabaseManager.getInstance().getWeeklyLessonDao().findByProfessor(cf, true));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else {
			// TODO: Handle user activities
		}
		return activities;
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
}
