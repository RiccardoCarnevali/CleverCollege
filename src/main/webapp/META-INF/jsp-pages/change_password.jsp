<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Change Password</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/login.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="/js/account_recovery.js"></script>
<script src="/js/common.js"></script>
</head>
<body>
<div class="container">
		<h5>
			Email di recupero inviata a
			<c:out value="${recovery_email}" />
		</h5>
		<p>Inserisci il token e la tua nuova password.</p>
		<form method="post" action="changePassword">
			<div class="mb-3">
				<label for="tokenInput" class="form-label"><strong>Token:</strong></label>
				<input type="text" class="form-control" id="tokenInput"
					placeholder="Token" name="token">
			</div>
			<div class="mb-3" id="password">
				<label for="passwordInput" class="form-label"><strong>Password:</strong></label>
				<input type="password" class="form-control" id="passwordInput"
					placeholder="Password" name="newPassword">
			</div>
			<div class="mb-3" id="passwordRepeat">
				<label for="passwordRepeatInput" class="form-label"><strong>Ripeti password:</strong></label>
				<input type="password" class="form-control" id="passwordRepeatInput"
					placeholder="Ripeti password" name="newPasswordConfirm">
			</div>
				<input type="hidden" class="form-control" name="mail" value="${recovery_email}">
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
</body>
</html>