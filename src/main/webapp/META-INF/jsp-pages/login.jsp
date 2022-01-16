<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
	    <title>Login</title>
	    <meta charset="UTF-8">
	    <meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width' name='viewport'>
	
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="//use.fontawesome.com/releases/v5.13.0/css/all.css">
	    <link rel="stylesheet" href="/css/common.css">
		<link rel="stylesheet" href="/css/login.css">
	
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	    <script src="/js/common.js"></script>
	    <script src="/js/login.js"></script>
		<script src="/js/generic_error.js" charset="UTF-8"></script>
	
	    <script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"></script>
	    <script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"></script>
	    <script src="/js/notification.js" charset="UTF-8"></script>
	
	</head>
	<body>
		<div class="header"></div>
		<div class="container">
		    <form method="post" action="doLogin">
		        <div id="second-column">
		            <div class="mb-3 mt-3" id="fiscalCode">
		                <label for="fiscalCodeInput" class="form-label"><strong>Codice fiscale:</strong></label>
		                <input type="text" class="form-control <c:if test="${(no_existing_user_error != null && no_existing_user_error == true) || (password_error != null && password_error == true)}">error-color</c:if>" id="fiscalCodeInput" placeholder="Inserisci il tuo codice fiscale..." name="cf">
		            </div>
		            <div class="mb-3" id="password">
		                <label for="passwordInput" class="form-label"><strong>Password:</strong></label>
		                <div style="position:relative">
			                <input type="password" class="form-control" id="passwordInput" maxlength="30" placeholder="Inserisci la tua password..." name="password">
			                <i class="fas fa-eye-slash clickable" id="see-password"></i>
		                </div>
		            </div>
		            <div class="mb-3">
		                <a href="account-recovery">Non riesci ad accedere? Clicca qui.</a>
		            </div>
		            <button type="submit" class="btn btn-outline-primary" id="login">
		                Login
		            </button>
		         </div>
		    </form>
		</div>
		
		<div class="footer"></div>
	</body>
</html>