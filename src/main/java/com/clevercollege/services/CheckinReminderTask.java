package com.clevercollege.services;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.NotificationToken;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.persistence.DatabaseManager;

public class CheckinReminderTask implements Runnable {
	
	private String notificationTitle;
	private String notificationBody;
	private long activityId;
	
	public CheckinReminderTask(SingleLesson singleLesson) {
		notificationTitle = "La lezione sta per cominciare!";
		notificationBody = "La lezione di " + singleLesson.getCourse().getName() + " sta per cominciare in " + 
							singleLesson.getClassroom().getName() + ". Ricorda di fare il check-in!";
		activityId = singleLesson.getId();
	}
	
	public CheckinReminderTask(Seminar seminar) {
		notificationTitle = "Il seminario sta per cominciare!";
		notificationBody = "Il seminario di oggi sta per cominciare in " + seminar.getClassroom().getName() + ". Ricorda di fare il check-in";
		activityId = seminar.getId();
	}
	
	@Override
	public void run() {
		try {
			List<NotificationToken> notificationTokens = DatabaseManager.getInstance().getNotificationTokenDao().findTokensForActivity(activityId);
			NotificationService.getInstance().sendNotificationTo(notificationTitle, notificationBody, notificationTokens);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		NotificationService.getInstance().getSchedule().remove(activityId + "in");
	}

}
