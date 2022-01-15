<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Change Password</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="//use.fontawesome.com/releases/v5.13.0/css/all.css">
		<link rel="stylesheet" href="/css/common.css">
		<link rel="stylesheet" href="/css/login.css">
		<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width' name='viewport'>
		
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
		<div class="container" id="changePasswordContainer">
	
			<h5>Un codice di recupero è stato inviato alla mail inserita</h5>
			<p>Inserisci il codice e la tua nuova password.</p>
			<form method="post" action="reset-password">
		
				<div class="mb-3" id="email">
					<label for="emailInput" class="form-label"><strong>Email:</strong></label>
					<input type="text" class="form-control <c:if test="${ (no_existing_mail_error != null && no_existing_mail_error == true) || (wrong_mail_error != null && wrong_mail_error == true)}">error-color</c:if>" id="emailInput" placeholder="Email" name="email"
					value="<c:if test="${recovery_email != null}">${recovery_email}</c:if>">
					<c:if test="${ no_existing_mail_error != null && no_existing_mail_error == true}"><span class="error-color" style="font-size: 0.8em; color: red;">La mail inserita non corrisponde ad alcun utente.</span></c:if>
					<c:if test="${ wrong_mail_error != null && wrong_mail_error == true}"><span class="error-color" style="font-size: 0.8em; color: red;">La mail inserita non corrisponde ad alcuna richiesta di recupero della password.</span></c:if>
				</div>
			
				<div class="mb-3" id="token">
					<label for="tokenInput" class="form-label"><strong>Codice:</strong></label>
					<input type="text" class="form-control" id="tokenInput"
						placeholder="Token" name="token">
				</div>
				<div class="mb-3" id="password">
					<label for="passwordInput" class="form-label"><strong>Password:</strong></label>
					<div style="position:relative">
						<input type="password" class="form-control" id="passwordInput"
							placeholder="Password" name="newPassword" maxlength="30">
						<i class="fas fa-eye-slash clickable" id="see-password"></i>
					</div>
				</div>
				<div class="mb-3" id="passwordRepeat">
					<label for="passwordRepeatInput" class="form-label"><strong>Ripeti password:</strong></label>
					<div style="position:relative">
						<input type="password" class="form-control" id="passwordRepeatInput"
							placeholder="Ripeti password" name="newPasswordConfirm"  maxlength="30">
						<i class="fas fa-eye-slash clickable" id="see-password-repeat"></i>
					</div>
				</div>
				<button type="submit" class="btn btn-outline-primary" id="changePasswordButton">Crea nuova password</button>
			</form>
			
		</div>
	
		<c:choose>
			<c:when test="${password_reset != null && password_reset}">
				<script type="text/javascript">fireSuccessAlert();</script>
			</c:when>
			<c:when test="${password_reset != null && !password_reset}">
				<script type="text/javascript">fireWrongTokenAlert();</script>
			</c:when>
			<c:when test="${password_reset != null && token_expired}">
				<script type="text/javascript">fireTokenExpiredAlert();</script>
			</c:when>
		</c:choose>
		<div class="footer"></div>
	</body>
</html>