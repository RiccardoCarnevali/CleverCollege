package com.clevercollege.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.clevercollege.model.RecoveryToken;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@Controller
public class AccountRecoveryController {
	@GetMapping("/account-recovery")
	public String accountRecovery(Model model) {
		model.addAttribute("email_sent", null);
		model.addAttribute("recovery_email", null);
		model.addAttribute("password_reset", null);
		return "account_recovery";
	}
	
	@PostMapping("/recoverPassword")
	public String recoverPassword(Model model, String email) {
		try {
			User user = DatabaseManager.getInstance().getUserDao().findByEmail(email);
			
			String token = UUID.randomUUID().toString();
			RecoveryToken recoveryToken = new RecoveryToken(BCrypt.hashpw(token, BCrypt.gensalt(12)), user.getCf());
			DatabaseManager.getInstance().getRecoveryTokenDao().saveOrUpdate(recoveryToken);
			DatabaseManager.getInstance().commit();
			
			//invia email con recovery token
			EmailService.getInstance().sendResetTokenEmail(token, email, user != null);
			model.addAttribute("email_sent", true);
			model.addAttribute("recovery_email", email);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "change_password";
	}
	
	
	@PostMapping("/changePassword")
	public String checkToken(Model model, String token, String newPassword, String mail) {
		model.addAttribute("recovery_email",mail);
		try {
			User user = DatabaseManager.getInstance().getUserDao().findByEmail(mail);
			RecoveryToken recoveryToken = DatabaseManager.getInstance().getRecoveryTokenDao().findByPrimaryKey(user.getCf());
			if(recoveryToken.getExpiryDate().isBefore(LocalDate.now())) {
				model.addAttribute("token_expired", true);
				model.addAttribute("password_reset", false);
				DatabaseManager.getInstance().getRecoveryTokenDao().delete(user.getCf());
				DatabaseManager.getInstance().commit();
			}
			else if(BCrypt.checkpw(token, recoveryToken.getToken())) {
				user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
				DatabaseManager.getInstance().getUserDao().saveOrUpdate(user);
				DatabaseManager.getInstance().getRecoveryTokenDao().delete(user.getCf());
				DatabaseManager.getInstance().commit();
				model.addAttribute("password_reset", true);
			} else {
				model.addAttribute("password_reset", false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "change_password";
		
		
	}
}
