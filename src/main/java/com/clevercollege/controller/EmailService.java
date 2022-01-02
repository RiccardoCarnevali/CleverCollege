package com.clevercollege.controller;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailService {

	private static EmailService instance = null;

	private JavaMailSenderImpl emailSender;

	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("noreply.clevercollege@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

	public void sendResetTokenEmail(String token, String to, boolean userExists) {

		String subject = "Ripristino password CleverCollege";
		String text;
		if (userExists) {
			text = "Usa il seguente token per ripristinare la password del tuo account CleverCollege: " + token
					+ System.lineSeparator() + "Se non sei stato tu a chiedere il "
					+ "reset della password ignora questa email.";
		} else {
			text = "É stato richiesto il ripristino della password per " + to + ", ma questa email non è registrata.";
		}
		sendEmail(to, subject, text);
	}

	public void sendFirstPassword(String to, String tmpPasswordToken) {
		String subject = "Accesso in CleverCollege";
		String text = "I tuoi dati sono stati inseriti e registrati all'interno dell'applicazione web Clever College, " +
					"usa il tuo codice fiscale e la seguente password per autenticarti. " +
					System.lineSeparator() + "Password temporanea: " + tmpPasswordToken + System.lineSeparator() +
					"Ricorda di modificare la password al tuo primo accesso.";

		sendEmail(to, subject, text);
	}


	private EmailService() {
		emailSender = new JavaMailSenderImpl();

		emailSender.setHost("smtp.gmail.com");
		emailSender.setPort(587);

		emailSender.setUsername("noreply.clevercollege@gmail.com");
		emailSender.setPassword("zGr\"=:(m/2U");

		Properties props = emailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
	}

	public static EmailService getInstance() {
		if (instance == null)
			instance = new EmailService();
		return instance;
	}
}