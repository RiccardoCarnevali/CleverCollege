<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width'
				name='viewport'>
		<title>Recupera Account</title>
		
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="/css/common.css">
		<link rel="stylesheet" href="/css/login.css">
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
		<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
		
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"></script>
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"></script>
		<script src="/js/notification.js" charset="UTF-8"></script>
		<script src="/js/common.js"></script>
		<script src="/js/generic_error.js" charset="UTF-8"></script>
		<script src="/js/account_recovery.js"></script>
	
	</head>
	
	<body>
	
		<div class="header"></div>
		<div
			class="container<c:if test="${email_sent != null || password_reset != null}"> hide</c:if>">
			<form method="post" action="recover-password">
				<div class="mb-3" id="insertEmail">
					<p>Hai dimenticato la tua password? Inserisci la tua email e
						ricevererai un codice per recuperarla.</p>
					<label for="emailInput" class="form-label"><strong>Email:</strong></label>
					<input type="text" class="form-control <c:if test="${ no_existing_mail_error != null && no_existing_mail_error == true}">error-color</c:if>" id="emailInput" placeholder="Email" name="email">
					<c:if test="${ no_existing_mail_error != null && no_existing_mail_error == true}"><span class="error-color mail-error">La mail inserita non corrisponde ad alcun utente.</span></c:if>
				</div>
				<button type="submit" class="btn btn-outline-primary"
					id="recoverAccountButton">Richiedi reset password</button>
	
			</form>
		</div>
		<div class="footer"></div>
	
	</body>
</html>