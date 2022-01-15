package com.clevercollege.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.clevercollege.model.NotificationToken;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.NotificationTokenDao;

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
		NotificationTokenDao notificationTokenDao = DatabaseManager.getInstance().getNotificationTokenDao();
		try {
			List<NotificationToken> notificationTokens = notificationTokenDao.findTokensForActivityStillInClassroom(activityId);
			
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send");
			httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.addHeader("Authorization", "key=AAAA_K0h54U:APA91bH5aq1AvSHnxraq2TRTsDTbNDpq2eXr2gQ7HA-Val-iuNwUbWGHMi7Ykq33blIOu_72zN4HsB1CeLNF0F2ysVfjxvmIA7pgsNjNlID6HaK6jB-MsJAkJ4XeLsxxYUQe-juIPrfC");
			for(NotificationToken notToken : notificationTokens) {
				String json = "{\"notification\": {\"title\": \"" + notificationTitle + "\",\"body\": \"" + notificationBody + "\",\"icon\": \"/assets/images/cc-logo.png\"},\"to\": \"" + notToken.getToken() + "\",\"time_to_live\": 0}";
				httpPost.setEntity(new StringEntity(json));
				CloseableHttpResponse response = client.execute(httpPost);
				String responseObjectJson = EntityUtils.toString(response.getEntity());
				if(responseObjectJson.contains("InvalidRegistration") || responseObjectJson.contains("NotRegistered"))
					notificationTokenDao.delete(notToken);
			}
			
			DatabaseManager.getInstance().commit();
			client.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		NotificationService.getInstance().getSchedule().remove(activityId + "out");
	}

}
