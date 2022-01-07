package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.Student;
import com.clevercollege.persistence.DatabaseManager;

@RestController
public class InsertDataController {

	@PostMapping("/setFollowedCourse")
	public String setFollowedCourse(Long courseId, Boolean followed, HttpServletRequest req) {

		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || !user_type.equals("student") || courseId == null || followed == null)
			return "error";

		Student student = (Student) session.getAttribute("user");

		if (student == null)
			return "error";

		try {
			if (followed) {
				DatabaseManager.getInstance().getStudentDao().followCourseForStudent(courseId, student.getCf());
				student.getFollowedCourses()
						.add(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(courseId));
			} else {
				DatabaseManager.getInstance().getStudentDao().unfollowCourseForStudent(courseId, student.getCf());
				student.getFollowedCourses()
						.remove(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(courseId));
			}
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			return "error";
		}

		return "ok";
	}

	@PostMapping("/checkCollidingActivities")
	public boolean checkCollidingActivities(Long activityId, HttpServletRequest req) {

		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || !user_type.equals("student") || activityId == null)
			return false;

		Student student = (Student) session.getAttribute("user");

		if (student == null)
			return false;

		List<Seminar> collidingSeminars = null;
		List<SingleLesson> collidingSingleLessons = null;

		try {
			SingleLesson singleLesson = DatabaseManager.getInstance().getSingleLessonDao().findByPrimaryKey(activityId,
					true);

			if (singleLesson != null) {
				collidingSingleLessons = DatabaseManager.getInstance().getSingleLessonDao()
						.findByCollidingTimeForStudent(singleLesson.getDate(), singleLesson.getTime(),
								singleLesson.getLength(), student.getCf(), true);

				if (collidingSingleLessons.size() != 0)
					return true;

				collidingSeminars = DatabaseManager.getInstance().getSeminarDao().findByCollidingTimeForStudent(
						singleLesson.getDate(), singleLesson.getTime(), singleLesson.getLength(), student.getCf(),
						true);

				if (collidingSeminars.size() != 0)
					return true;
			} else {
				Seminar seminar = DatabaseManager.getInstance().getSeminarDao().findByPrimaryKey(activityId, true);

				if (seminar == null)
					return false;

				collidingSingleLessons = DatabaseManager.getInstance().getSingleLessonDao()
						.findByCollidingTimeForStudent(seminar.getDate(), seminar.getTime(), seminar.getLength(),
								student.getCf(), true);

				if (collidingSingleLessons.size() != 0)
					return true;

				collidingSeminars = DatabaseManager.getInstance().getSeminarDao().findByCollidingTimeForStudent(
						seminar.getDate(), seminar.getTime(), seminar.getLength(), student.getCf(), true);
				
				if(collidingSeminars.size() != 0)
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	@PostMapping("/setBookedActivity")
	public String bookLesson(Long activityId, Boolean booked, HttpServletRequest req) {
		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || !user_type.equals("student") || activityId == null || booked == null)
			return "error";

		Student student = (Student) session.getAttribute("user");

		if (student == null)
			return "error";

		try {
			if (booked) {
				if (!DatabaseManager.getInstance().getStudentDao().bookActivityForStudent(activityId, student.getCf())) {
					return "maximum capacity";
				}
			} else {
				DatabaseManager.getInstance().getStudentDao().unbookActivityForStudent(activityId, student.getCf());
			}
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		return "ok";
	}

}
