<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <title>Studenti in aula</title>
    <meta charset="UTF-8">
    <meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width' name='viewport'>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="//use.fontawesome.com/releases/v5.13.0/css/all.css">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/view_data.css">
    <link rel="stylesheet" href="/css/view_users.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="/js/common.js"></script>
    <script src="/js/generic_error.js" charset="UTF-8"></script>
</head>
<body>
<div class="header"></div>
<div class="container-fluid">

    <span id="label">Studenti in aula</span>
    <input class="form-control col-lg-4 col-md-6 col-sm-11 col-11" type="search" id="searchBar" placeholder="Cerca">
    <i class="fas fa-search" id="search-icon"></i>

    <select class="form-control col-lg-2 col-md-6 col-sm-12" id="sortInput">
        <option selected disabled value="cf">Ordina per</option>
        <option value="first_name">Nome</option>
        <option value="last_name">Cognome</option>
    </select>

    <ul class="list-group" id="student-checked-in">
        <li class='list-group-item location'>
            <span class='location-name'>loc1</span>
            <div class='icons' style='float:right'>
                <span class='clickable modify-button' id='modify-' style='display:block;margin-bottom:20px'><i class='fas fa-pen'></i></span>
                <span class='clickable remove-button' id='remove-'><i class='fas fa-trash'></i></span>
            </div>
            <span class='location-capacity' style='display:block'>Capacità massima: 10 persone</span>
        </li>
    </ul>
</div>
<div class="footer"></div>
</body>
</html>