package com.clevercollege.model;

import java.util.Objects;

public class User {

	private String cf;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String description;
	private String profilePicture;

	public User() {

	}

	public User(String cf, String firstName, String lastName, String email, String password, String description,
			String profilePicture) {
		super();
		this.cf = cf;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.description = description;
		this.profilePicture = profilePicture;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(cf, other.cf);
	}

	@Override
	public String toString() {
		return "User [cf=" + cf + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", description=" + description + ", profilePicture=" + profilePicture
				+ "]";
	}
	
	
}
