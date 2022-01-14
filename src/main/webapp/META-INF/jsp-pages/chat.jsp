<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width' name='viewport'>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/chat.css">

    <script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"></script>
    <script src="/js/notification.js" charset="UTF-8"></script>
    <script src="/js/common.js"></script>
    <script src="/js/generic_error.js" charset="UTF-8"></script>
    <script src="/js/chat.js" charset="UTF-8"></script>
    <title>Chat</title>
</head>
<body>
    <div class="header"></div>
    <div class="container">Nome del receiver e ruolo</div>
    <div class="container">
        <div class="chat-frame">
            <ul class="all-messages" id="messages-${cfUserSend}-${cfUserRec}">
                <li class="single-messages">Prova</li>
            </ul>
            <div style="display: flex">
                <textarea id="message-input" placeholder="Inserisci qui il tuo messaggio"></textarea>
                <button type="button" id="sendMessage"></button>
            </div>
        </div>
    </div>
    <div class="footer"></div>
</body>
</html>