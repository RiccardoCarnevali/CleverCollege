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
		NotificationTokenDao notificationTokenDao = DatabaseManager.getInstance().getNotificationTokenDao();
		try {
			List<NotificationToken> notificationTokens = notificationTokenDao.findTokensForActivity(activityId);
			
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send"); 
			httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.addHeader("Authorization", "key=AAAA_K0h54U:APA91bH5aq1AvSHnxraq2TRTsDTbNDpq2eXr2gQ7HA-Val-iuNwUbWGHMi7Ykq33blIOu_72zN4HsB1CeLNF0F2ysVfjxvmIA7pgsNjNlID6HaK6jB-MsJAkJ4XeLsxxYUQe-juIPrfC");
			for(NotificationToken notToken : notificationTokens) {
				String json = "{\"notification\": {\"title\": \"" + notificationTitle + "\",\"body\": \"" + notificationBody + "\",\"icon\": \"/assets/images/cc-logo.png\"},\"to\": \"" + notToken.getToken() + "\",\"time_to_live\": 0}";
				httpPost.setEntity(new StringEntity(json));
				CloseableHttpResponse response = client.execute(httpPost);
				String responseObjectJson = EntityUtils.toString(response.getEntity());
				if(responseObjectJson.contains("InvalidRegistration"))
					notificationTokenDao.delete(notToken);
			}
			
			DatabaseManager.getInstance().commit();
			client.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

}
