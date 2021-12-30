package com.clevercollege.model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.clevercollege.persistence.DatabaseManager;


public class RecoveryToken {
 
	//scadenza in giorni
    private static final int EXPIRATION = 1;

    private Long id;
 
    private String token;
 
    private String cf;
 
    private LocalDate expiryDate;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public RecoveryToken(String token, String cf) {
    	this.token = token;
    	this.cf = cf;
    	try {
			this.id = DatabaseManager.getInstance().getIdBroker().getNextPasswordTokenId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	calculateExpiryDate();
    }

	public RecoveryToken() {
		
	}

	public void calculateExpiryDate() {
        expiryDate = LocalDate.now().plus(EXPIRATION, ChronoUnit.DAYS);
	}
	
	public void updateToken(final String token) {
        this.token = token;
        calculateExpiryDate();
    }
}