<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <title>Insert user</title>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/insert.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="/js/common.js"></script>
    <script src="/js/insertUserField.js"></script>

<body>
<div class="header"></div>
<div class="padding-div">
    <div class="container">
        <div class="first-column">
            <form>
                <p>Scegli il tipo di utente che desideri inserire:</p>
                <div class="radio-item">
                    <input type="radio" id="student" name="kindOfUser" value="student">
                    <label for="student">Studente</label><br>
                </div>
                <div class="radio-item">
                    <input type="radio" id="professor" name="kindOfUser" value="professor" checked>
                    <label for="professor">Professore</label><br>
                </div>
                <div class="radio-item">
                    <input type="radio" id="admin" name="kindOfUser" value="admin">
                    <label for="admin">Amministratore</label>
                </div>
            </form>
        </div>
        <div class="second-column">
            <form>
                <div class="mb-3 mt-3">
                    <label for="fiscalCodeUser" class="form-label">Codice fiscale:</label>
                    <input type="text" class="form-control insert-user-element" id="fiscalCodeUser" placeholder="Inserisci il codice fiscale..." name="cf">
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label">Nome:</label>
                    <input type="text" class="form-control insert-user-element" id="name" placeholder="Inserisci il nome..." name="name">
                </div>
                <div class="mb-3">
                    <label for="surname" class="form-label">Cognome:</label>
                    <input type="text" class="form-control insert-user-element" id="surname" placeholder="Inserisci il cognome..." name="surname">
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email istituzionale:</label>
                    <input type="email" class="form-control insert-user-element" id="email" placeholder="Inserisci l'email..." name="email">
                </div>
                <div class="mb-3">
                    <label id="label-for-Student" for="idStudent" class="form-label" style="display: none">Matricola:</label>
                    <input type="text" class="form-control insert-user-element" id="idStudent" placeholder="Inserisci la matricola..." name="idStudent" style="display: none">
                </div>
                <div class="button-container">
                    <button type="button" id="insert-user-button" class="btn btn-outline-primary" disabled>Inserisci utente</button>
                    <div class="spinner-border loader" style="display: none;"></div>
                    <button type="button" id="cancel-insertion" class="btn btn-outline-danger danger-button">Annulla inserimento</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="footer"></div>
</body>
</html>