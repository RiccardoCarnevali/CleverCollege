<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Recupera Account</title>

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

	<div class="header"></div>
	<div
		class="container<c:if test="${email_sent != null || password_reset != null}"> hide</c:if>">
		<form method="post" action="recoverPassword">
			<div class="mb-3" id="insertEmail">
				<p>Hai dimenticato la tua password? Inserisci la tua email e
					ricevererai un link per recuperarla.</p>
				<label for="emailInput" class="form-label"><strong>Email:</strong></label>
				<input type="text" class="form-control" id="emailInput"
					placeholder="Email" name="email">
			</div>
			<button type="submit" class="btn btn-outline-primary"
				id="recoverAccountButton">Richiedi reset password</button>

		</form>
	</div>
	<div class="footer"></div>

</body>
</html>