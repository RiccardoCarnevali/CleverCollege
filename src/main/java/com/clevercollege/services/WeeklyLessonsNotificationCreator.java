package com.clevercollege.services;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.clevercollege.model.SingleLesson;
import com.clevercollege.persistence.DatabaseManager;

@Service
public class WeeklyLessonsNotificationCreator {

	@Scheduled(cron = "0 0 1 ? * SAT")
	public void createNotificationTasksForWeeklyLessons() {
		
		Map<String, ScheduledFuture<?>> schedule = NotificationService.getInstance().getSchedule();
		
		try {
			List<SingleLesson> singleLessons = DatabaseManager.getInstance().getSingleLessonDao().findNotExpired(true);
			
			for(SingleLesson singleLesson : singleLessons) {
				if(schedule.get(singleLesson.getId() + "in") == null) 
					NotificationService.getInstance().schedule(new CheckinReminderTask(singleLesson),
							LocalDateTime.of(LocalDate.parse(singleLesson.getDate()), LocalTime.parse(singleLesson.getTime())).minusMinutes(5), singleLesson.getId() + "in");
				if(schedule.get(singleLesson.getId() + "out") == null) 
					NotificationService.getInstance().schedule(new CheckoutReminderTask(singleLesson), 
							LocalDateTime.of(LocalDate.parse(singleLesson.getDate()), LocalTime.parse(singleLesson.getTime())).plusMinutes(singleLesson.getLength()), singleLesson.getId() + "out");
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
