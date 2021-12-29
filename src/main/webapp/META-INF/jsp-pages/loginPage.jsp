<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/index.css">
	<link rel="stylesheet" href="/css/loginPage.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="/js/header.js"></script>
    <script src="/js/loginPage.js"></script>
</head>
<body>
<div class="header"></div>
<div class="container">
    <form method="post" action="doLogin">
        <div id="second-column">
            <div class="mb-3 mt-3" id="fiscalCode">
                <label for="fiscalCode" class="form-label">Codice fiscale:</label>
                <input type="text" class="form-control <c:if test="${no_existing_user_error != null && no_existing_user_error == true}">error-color</c:if>" id="fiscalCodeInput" placeholder="Inserisci il tuo codice fiscale..." name="cf">
            </div>
            <div class="mb-3" id="password">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" id="passwordInput" placeholder="Inserisci la tua password..." name="password">
            </div>
            <div class="mb-3">
                <a href="#">Non riesci ad accedere? Clicca qui.</a>
            </div>
            <button type="submit" class="btn btn-outline-primary" id="login">
                Login
            </button>
         </div>
    </form>
</div>
<div class="container-fluid">
    <h2>Non riesci ad accedere?</h2>
    <section class="row">
        <article class="col-md-6 col-lg-6">
            <h3>La tua password sembra errata?</h3>
            <p>
                Controlla accuratamente che tu abbia digitato correttamente la password o che non ci sia il caps lock attivato impropriamente.
                Se questo è il tuo primo accesso, controlla di aver inserito correttamente la password a te inviata dall'amministratore via email.
                Se non hai ricevuto alcuna email, contatta la segreteria studenti.
            </p>
        </article>
        <article class="col-md-6 col-lg-6">
            <h3>Il tuo codice fiscale sembra errato?</h3>
            <p>
                Controlla accuratamente che tu abbia digitato correttamente il codice fiscale.
                Se questo è il tuo primo accesso, assicurati prima di aver ricevuto la email a te inviata dall'amministratore che assicura che tu
                sia stato registrato.
                Se non hai ricevuto alcuna email, contatta la segreteria studenti.
            </p>
        </article>
    </section>
</div>
<footer class="container-fluid">
    Lorem ipsum dolor sit amet, consectetur
    adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore
    magna aliqua. Lectus mauris ultrices eros in cursus turpis. Egestas
    sed tempus urna et pharetra pharetra massa. Massa sapien faucibus et
    molestie ac feugiat sed. Maecenas ultricies mi eget mauris pharetra
    et. Tellus mauris a diam maecenas sed enim ut sem.
</footer>
</body>
</html>