package com.clevercollege.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.clevercollege.model.NotificationToken;
import com.clevercollege.persistence.DatabaseManager;

@Service
public class NotificationService {
	
	private static NotificationService instance = null;
	
	private ScheduledThreadPoolExecutor executor;
	
	private Map<String, ScheduledFuture<?>> schedule;
	
	private NotificationService() {
		executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		executor.setRemoveOnCancelPolicy(true);
		
		schedule = new HashMap<>();
	}
	
	public static NotificationService getInstance() {
		if(instance == null)
			instance = new NotificationService();
		return instance;
	}
	
	public void schedule(Runnable task, LocalDateTime localDateTime, String key) {
		schedule.put(key, executor.schedule(task, LocalDateTime.now().until(localDateTime, ChronoUnit.SECONDS), TimeUnit.SECONDS));
	}
	
	public void cancelSchedule(String key) {
		ScheduledFuture<?> scheduledNotification = schedule.get(key);
		if(scheduledNotification != null) {
			scheduledNotification.cancel(false);
			schedule.remove(key);
		}
	}
	
	public Map<String, ScheduledFuture<?>> getSchedule() {
		return schedule;
	}

	public void sendNotificationToAll() {
		try {
			List<NotificationToken> notificationTokens = DatabaseManager.getInstance().getNotificationTokenDao().findAll();
			sendNotificationTo("Notifica per tutti", "Questa e' una notifica per tutti gli utenti", notificationTokens);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void sendNotificationTo(String title, String body, List<NotificationToken> notificationTokens) {
		try {
			
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send");
			httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.addHeader("Authorization", "key=AAAA_K0h54U:APA91bH5aq1AvSHnxraq2TRTsDTbNDpq2eXr2gQ7HA-Val-iuNwUbWGHMi7Ykq33blIOu_72zN4HsB1CeLNF0F2ysVfjxvmIA7pgsNjNlID6HaK6jB-MsJAkJ4XeLsxxYUQe-juIPrfC");
			
			for(NotificationToken notToken : notificationTokens) {
				String json = "{\"notification\": {\"title\": \"" + title + "\",\"body\": \"" + body + "\",\"icon\": \"/assets/images/cc-logo.png\"},\"to\": \"" + notToken.getToken() + "\",\"time_to_live\": 0}";
				httpPost.setEntity(new StringEntity(json));
				CloseableHttpResponse response = client.execute(httpPost);
				String responseObjectJson = EntityUtils.toString(response.getEntity());
				if(responseObjectJson.contains("InvalidRegistration") || responseObjectJson.contains("NotRegistered"))
					DatabaseManager.getInstance().getNotificationTokenDao().delete(notToken);
			}
			
			DatabaseManager.getInstance().commit();
			client.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
