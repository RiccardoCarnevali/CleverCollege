<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">
	<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width'
		name='viewport'>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/common.css">
	<link rel="stylesheet" href="/css/login.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="/js/common.js"></script>
    <script src="/js/login.js"></script>
</head>
<body>
<div class="header"></div>
<div class="container">
    <form method="post" action="doLogin">
        <div id="second-column">
            <div class="mb-3 mt-3" id="fiscalCode">
                <label for="fiscalCodeInput" class="form-label"><strong>Codice fiscale:</strong></label>
                <input type="text" class="form-control <c:if test="${no_existing_user_error != null && no_existing_user_error == true}">error-color</c:if>" id="fiscalCodeInput" placeholder="Inserisci il tuo codice fiscale..." name="cf">
            </div>
            <div class="mb-3" id="password">
                <label for="passwordInput" class="form-label"><strong>Password:</strong></label>
                <input type="password" class="form-control" id="passwordInput" placeholder="Inserisci la tua password..." name="password">
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
