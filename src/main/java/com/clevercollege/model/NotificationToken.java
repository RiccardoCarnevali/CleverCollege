package com.clevercollege.model;

import java.util.Objects;

public class NotificationToken {

	private String user;
	private String token;
	
	public NotificationToken() {
		
	}
	
	public NotificationToken(String user, String token) {
		super();
		this.user = user;
		this.token = token;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		return Objects.hash(token, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationToken other = (NotificationToken) obj;
		return Objects.equals(token, other.token) && Objects.equals(user, other.user);
	}
	
}
