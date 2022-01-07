package com.clevercollege.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
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
import com.google.gson.Gson;

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
	public void enableWeeklyLesson(HttpServletRequest request, Long id, Boolean disable, Boolean indefinite) {
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
	public String createActivity(HttpServletRequest request, String jsonString, String type, Boolean edit) {
		if (jsonString == null || type == null) {
			return null;
		}
		HttpSession session = request.getSession();
		if (session.getAttribute("user_type") == null || session.getAttribute("user") == null
				|| !("professor").equals(session.getAttribute("user_type"))) {
			return null;
		}
		JSONObject postResults = new JSONObject();
		try {
			ObjectMapper mapper = new ObjectMapper();
			User user = (User) session.getAttribute("user");
			Long id = null;
			if (!edit) {
				id = DatabaseManager.getInstance().getIdBroker().getNextActivityId();
			}

			if (type.equals("single")) {
				SingleLesson single = mapper.readValue(jsonString, SingleLesson.class);
				if (!edit) {
					single.setId(id);
				}
				else {
					DatabaseManager.getInstance().getSingleLessonDao().delete(single.getId());
					DatabaseManager.getInstance().getSeminarDao().delete(single.getId());
					DatabaseManager.getInstance().getWeeklyLessonDao().delete(single.getId());
				}
				single.setManager(user);
				if (!single.checkValid()) {
					return null;
				}
				String conflictActivity = checkProfessorActivityConflict(single, LocalDate.parse(single.getDate()),
						user);

				if (conflictActivity != null) {
					postResults.put("activity_conflict", new JSONObject(conflictActivity));
				} else {
					DatabaseManager.getInstance().getSingleLessonDao().saveOrUpdate(single);
				}
			} else if (type.equals("weekly")) {
				WeeklyLesson weekly = mapper.readValue(jsonString, WeeklyLesson.class);
				if (!edit) {
					weekly.setId(id);
				}
				else {
					DatabaseManager.getInstance().getSingleLessonDao().delete(weekly.getId());
					DatabaseManager.getInstance().getSeminarDao().delete(weekly.getId());
					DatabaseManager.getInstance().getWeeklyLessonDao().delete(weekly.getId());
				}
				weekly.setManager(user);
				if (!weekly.checkValid())
					return null;

				String conflictActivity = checkProfessorActivityConflict(weekly, user);

				if (conflictActivity != null) {
					postResults.put("activity_conflict", new JSONObject(conflictActivity));
				} else {
					DatabaseManager.getInstance().getWeeklyLessonDao().saveOrUpdate(weekly);
				}
			} else if (type.equals("seminar")) {
				Seminar seminar = mapper.readValue(jsonString, Seminar.class);
				if (!edit) {
					seminar.setId(id);
				} else {
					DatabaseManager.getInstance().getSingleLessonDao().delete(seminar.getId());
					DatabaseManager.getInstance().getSeminarDao().delete(seminar.getId());
					DatabaseManager.getInstance().getWeeklyLessonDao().delete(seminar.getId());
				}
				seminar.setManager(user);
				if (!seminar.checkValid()) {
					return null;
				}

				String conflictActivity = checkProfessorActivityConflict(seminar, LocalDate.parse(seminar.getDate()),
						user);

				if (conflictActivity != null) {
					postResults.put("activity_conflict", conflictActivity);
				} else {
					DatabaseManager.getInstance().getSeminarDao().saveOrUpdate(seminar);
				}
			}
			DatabaseManager.getInstance().commit();
			if(postResults.has("activity_conflict")) {
				return postResults.toString();
			}
			return null;
		} catch (SQLException | JsonProcessingException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String checkProfessorActivityConflict(WeeklyLesson weekly, User user) throws SQLException {
		Integer weekday1 = weekly.getWeekDay();

		LocalTime start1 = LocalTime.parse(weekly.getTime());
		LocalTime end1 = LocalTime.parse(weekly.getTime()).plusMinutes(weekly.getLength());

		List<WeeklyLesson> weeklies = DatabaseManager.getInstance().getWeeklyLessonDao().findByProfessor(user.getCf(),
				true);
		List<SingleLesson> singles = DatabaseManager.getInstance().getSingleLessonDao().findByProfessor(user.getCf(),
				true);
		List<Seminar> seminars = DatabaseManager.getInstance().getSeminarDao().findByProfessor(user.getCf(), true);

		Gson gson = new Gson();
		
		for (SingleLesson activity2 : singles) {
			LocalDate date2 = LocalDate.parse(activity2.getDate());
			int weekday2 = date2.getDayOfWeek().getValue() - 1;
			if (date2.isAfter(LocalDate.now()) && weekday2 == weekday1 && weekly.getId() != activity2.getId()) {
				if (timeIntersects(start1, end1, activity2)) {
					JSONObject obj = new JSONObject(gson.toJson(activity2));
					obj.put("activity_conflict_type", "single");
					return obj.toString();
				}
			}
		}

		for (Seminar activity2 : seminars) {
			LocalDate date2 = LocalDate.parse(activity2.getDate());
			int weekday2 = date2.getDayOfWeek().getValue() - 1;
			if (date2.isAfter(LocalDate.now()) && weekday2 == weekday1 && weekly.getId() != activity2.getId()) {
				if (timeIntersects(start1, end1, activity2)) {
					JSONObject obj = new JSONObject(gson.toJson(activity2));
					obj.put("activity_conflict_type", "seminar");
					return obj.toString();
				}
			}
		}

		for (WeeklyLesson activity2 : weeklies) {
			int weekday2 = activity2.getWeekDay();
			if (weekday2 == weekday1 && weekly.getId() != activity2.getId()) {
				if (timeIntersects(start1, end1, activity2)) {
					JSONObject obj = new JSONObject(gson.toJson(activity2));
					obj.put("activity_conflict_type", "weekly");
					return obj.toString();
				}
			}
		}
		return null;
	}

	private String checkProfessorActivityConflict(Activity activity1, LocalDate date1, User user) throws SQLException {

		LocalTime start1 = LocalTime.parse(activity1.getTime());
		LocalTime end1 = LocalTime.parse(activity1.getTime()).plusMinutes(activity1.getLength());

		List<WeeklyLesson> weeklies = DatabaseManager.getInstance().getWeeklyLessonDao().findByProfessor(user.getCf(),
				true);
		List<SingleLesson> singles = DatabaseManager.getInstance().getSingleLessonDao().findByProfessor(user.getCf(),
				true);
		List<Seminar> seminars = DatabaseManager.getInstance().getSeminarDao().findByProfessor(user.getCf(), true);

		Gson gson = new Gson();

		for (SingleLesson activity2 : singles) {
			if (date1.isEqual(LocalDate.parse(activity2.getDate())) && activity1.getId() != activity2.getId()) {
				if (timeIntersects(start1, end1, activity2)) {
					JSONObject obj = new JSONObject(gson.toJson(activity2));
					obj.put("activity_conflict_type", "single");
					return obj.toString();
				}
			}
		}

		for (Seminar activity2 : seminars) {
			if (date1.isEqual(LocalDate.parse(activity2.getDate())) && activity1.getId() != activity2.getId()) {
				if (timeIntersects(start1, end1, activity2)) {
					JSONObject obj = new JSONObject(gson.toJson(activity2));
					obj.put("activity_conflict_type", "seminar");
					return obj.toString();
				}
			}
		}

		for (WeeklyLesson activity2 : weeklies) {
			if ((date1.getDayOfWeek().getValue() - 1) == activity2.getWeekDay()
					&& activity1.getId() != activity2.getId()) {
				if (timeIntersects(start1, end1, activity2)) {
					JSONObject obj = new JSONObject(gson.toJson(activity2));
					obj.put("activity_conflict_type", "weekly");
					return obj.toString();
				}
			}
		}

		return null;
	}

	private boolean timeIntersects(LocalTime start1, LocalTime end1, Activity activity) {
		LocalTime start2 = LocalTime.parse(activity.getTime());
		LocalTime end2 = LocalTime.parse(activity.getTime()).plusMinutes(activity.getLength());
		return ((start1.isBefore(end2) && start2.isBefore(end1)));
	}

	@PostMapping("/deleteActivity")
	public void deleteActivity(HttpServletRequest request, Long id) {
		HttpSession session = request.getSession();
		String userType = (String) session.getAttribute("user_type");
		if (userType == null || session.getAttribute("user") == null
				|| (!("professor").equals(userType) && !("admin").equals(userType))) {
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
