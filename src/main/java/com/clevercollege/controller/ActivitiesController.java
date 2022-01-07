package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.WeeklyLesson;
import com.clevercollege.persistence.DatabaseManager;

@Controller
public class ActivitiesController {

	@GetMapping("/activities/handle_activities")
	public String handleActivities(HttpServletRequest request) {
		if (request.getSession().getAttribute("user_type") == null) {
			request.getSession().setAttribute("after-login", "/activities/handle_activities");
			return "redirect:/login";
		}

		if (request.getSession().getAttribute("user_type") != "professor") {
			return "/not_authorized";
		}

		return "/activities/handle_activities";
	}

	@GetMapping("/activities/create_activity")
	public String createActivity(HttpServletRequest request) {
		if (request.getSession().getAttribute("user_type") == null) {
			request.getSession().setAttribute("after-login", "/activities/create_activity");
			return "redirect:/login";
		}

		if (request.getSession().getAttribute("user_type") != "professor") {
			return "/not_authorized";
		}

		return "/activities/create_activity";
	}

	@PostMapping("/activities/edit_activity")
	public String editActivity(HttpServletRequest request, Long id, String type) {
		if (request.getSession().getAttribute("user_type") == null) {
			request.getSession().setAttribute("after-login", "/activities/create_activity");
			return "redirect:/login";
		}

		if (request.getSession().getAttribute("user_type") != "professor") {
			return "/not_authorized";
		}

		request.setAttribute("activity_type", type);

		try {
			if (type.equals("single")) {
				SingleLesson single = DatabaseManager.getInstance().getSingleLessonDao().findByPrimaryKey(id, true);
				request.setAttribute("activity", single);
			} else if (type.equals("weekly")) {
				WeeklyLesson weekly = DatabaseManager.getInstance().getWeeklyLessonDao().findByPrimaryKey(id);
				request.setAttribute("activity", weekly);
			} else if (type.equals("seminar")) {
				Seminar seminar = DatabaseManager.getInstance().getSeminarDao().findByPrimaryKey(id, true);
				request.setAttribute("activity", seminar);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/activities/create_activity";
	}

}
