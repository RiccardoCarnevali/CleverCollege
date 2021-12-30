<html>
<head>
	<title>Clever College - Area Personale</title>
	<meta charset="ISO-8859-1">
	<meta content="maximum-scale=1.0, initial-scale=1.0, width=device-width" name="viewport">
	
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	
	<link rel="stylesheet" href="/css/header.css">
	<link rel="stylesheet" href="/css/index.css">
	<link rel="stylesheet" href="/css/myprofile.css">
	
	<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	<script	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script src="/js/header.js"></script>
</head>

<body>
	<div class="header"></div>
		<div class="content container-fluid">
			<div id="mainTable">
				<h1>Attività prenotate</h1>
				<ul class="list-group" id="activeLessons">
					<li class="list-group-item">Non hai attività prenotate. Riposati o studia autonomamente!</li>
					<li class="list-group-item">
						<button type="button" class="btn btn-primary" id="check-in/out" href="/">Esegui check-in/out</button>
					</li>
				</ul>
			</div>
		
			<div class="card">
				<div id="info" class="card-body">
					<h2 class=""><img id="profile-picture" class="card-img-top rounded-circle" alt="ma guarda quanto sei bello/a" src="/assets/images/pp-placeholder.png"> Le tue informazioni personali</h2>
						<p class="card-text">Nome: <span id="name">${user.name}</span></p>
						<p class="card-text">Cognome: <span id="surname">${user.surname}</span></p>
						<p class="card-text">Codice Fiscale: <span id="cf">${user.cf}</span></p>
						<p class="card-text">E-mail: <span id="email">${user.email}</span></p>
						<p class="card-text" id="descrizione"></p>
						<button type="button" class="btn btn-primary card-link" id="changeDescription" href="/">Modifica Descrizione</button>
						<button type="button" class="btn btn-danger card-link" id="changePassword" href="/">Cambia password</button>
				</div>
			</div>
		</div>	
</body>

</html>