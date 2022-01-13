package com.clevercollege.services;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.persistence.DatabaseManager;

@Service
public class SchedulesRecoverer {

	public SchedulesRecoverer() {
		
		List<SingleLesson> singleLessons;
		List<Seminar> seminars;
		try {
			singleLessons = DatabaseManager.getInstance().getSingleLessonDao().findNotExpired(true);
			for(SingleLesson singleLesson : singleLessons) {
				NotificationService.getInstance().schedule(new CheckinReminderTask(singleLesson),
						LocalDateTime.of(LocalDate.parse(singleLesson.getDate()), LocalTime.parse(singleLesson.getTime())).minusMinutes(5), singleLesson.getId() + "in");
				NotificationService.getInstance().schedule(new CheckoutReminderTask(singleLesson), 
						LocalDateTime.of(LocalDate.parse(singleLesson.getDate()), LocalTime.parse(singleLesson.getTime())).plusMinutes(singleLesson.getLength()), singleLesson.getId() + "out");
			}
			
			seminars = DatabaseManager.getInstance().getSeminarDao().findNotExpired(true);
			for(Seminar seminar : seminars) {
				NotificationService.getInstance().schedule(new CheckinReminderTask(seminar), 
						LocalDateTime.of(LocalDate.parse(seminar.getDate()), LocalTime.parse(seminar.getTime())).minusMinutes(5), seminar.getId() + "in");
				NotificationService.getInstance().schedule(new CheckoutReminderTask(seminar), 
						LocalDateTime.of(LocalDate.parse(seminar.getDate()), LocalTime.parse(seminar.getTime())).plusMinutes(seminar.getLength()), seminar.getId() + "out");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
