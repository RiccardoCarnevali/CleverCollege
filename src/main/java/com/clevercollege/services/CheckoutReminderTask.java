package com.clevercollege.services;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.NotificationToken;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.persistence.DatabaseManager;

public class CheckoutReminderTask implements Runnable {

	private String notificationTitle;
	private String notificationBody;
	private long activityId;
	
	public CheckoutReminderTask(SingleLesson singleLesson) {
		notificationTitle = "La lezione e' finita!";
		notificationBody = "La lezione di " + singleLesson.getCourse().getName() + " e' finita, se non devi rimanere in "
							+ singleLesson.getClassroom().getName() + " ricorda di effettuare il check-out quando esci!";
		activityId = singleLesson.getId();
	}
	
	public CheckoutReminderTask(Seminar seminar) {
		notificationTitle = "Il seminario è finito!";
		notificationBody = "Il seminario di oggi è finito, se non devi rimanere in "
							+ seminar.getClassroom().getName() + " ricorda di effettuare il check-out quando esci!";
		activityId = seminar.getId();
	}
	
	@Override
	public void run() {
		try {
			List<NotificationToken> notificationTokens = DatabaseManager.getInstance().getNotificationTokenDao().findTokensForActivityStillInClassroom(activityId);
			NotificationService.getInstance().sendNotificationTo(notificationTitle, notificationBody, notificationTokens);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		NotificationService.getInstance().getSchedule().remove(activityId + "out");
	}

}
