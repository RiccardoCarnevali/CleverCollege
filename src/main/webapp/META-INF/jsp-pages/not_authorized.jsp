<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Errore</title>
		
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="//use.fontawesome.com/releases/v5.13.0/css/all.css">
		<link rel="stylesheet" href="/css/common.css">
		<link rel="stylesheet" href="/css/error.css">
		<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width' name='viewport'>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
		<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
		
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"></script>
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"></script>
		<script src="/js/notification.js" charset="UTF-8"></script>
		<script src="/js/common.js"></script>
		<script src="/js/generic_error.js" charset="UTF-8"></script>
	
	</head>
	<body>
	
		<div class="header"></div>
		<div class="container">
			<div id="not-authorized">Non puoi accedere a questa pagina</div>
			<div><i class="fas fa-user-lock"></i></div>
			<a href="/" class="btn btn-outline-primary">Torna alla home page</a>
		</div>
		<div class="footer"></div>
	
	</body>
</html>