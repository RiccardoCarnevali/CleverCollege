<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width' name='viewport'>
	
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">
	
		<link rel="stylesheet" href="/css/common.css">
		<link rel="stylesheet" href="/css/index.css">
	
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
		<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js" charset="UTF-8"></script>
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js" charset="UTF-8"></script>
		<script src="/js/notification.js" charset="UTF-8"></script>
		<script src="/js/common.js"></script>
		<script src="/js/generic_error.js" charset="UTF-8"></script>
	
		<title>Clever College</title>
	</head>
	
	<body>
		<div class="header"></div>
		<div class="container-fluid">
			<div class="row">
				<div class="col-lg-4 col-md-4 col-sm-6 center-object-img">
					<img src="assets/images/manyUser.png">
				</div>
				<div class="container col-lg-8 col-md-8 col-sm-6 center-object-text">
					<p> Clever College è una applicazione web nata dall'esigenza di rendere facile e intuitiva la vita all'interno
						degli spazi universitari. Per rendere ciò realtà, all'interno del sito sono presenti diverse funzioni che
						permettono a tutti coloro che fanno parte del Campus di gestire le proprie attività. </p>
				</div>
			</div>
		</div>
		<div class="container-fluid">
			<div class="row" id="second-container">
				<div class="container col-lg-4 col-md-4 col-sm-6 center-object-text">
					<p> Ogni studente può prenotare le lezioni e i seminari alle quali desidera partecipare,
						personalizzare il proprio profilo e fare check-in (e relativo check-out) nelle aule all'interno
						del quale si svolgono le attività o si può usufruire del servizio mensa.</p>
				</div>
				<div class="col-lg-8 col-md-8 col-sm-6 youtube-container center-object-text">
					<iframe src="https://www.youtube.com/embed/K70Zj8FxaZM"
							title="YouTube video player" frameborder="0" allow="accelerometer; autoplay;
								clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
					</iframe>
				</div>
			</div>
		</div>
		<div class="container-fluid">
			<div class="container center-object-text">
				<p>
					Per poter accedere a Clever College è necessario che, precedentemente, il personale amministrativo si sia
					occupato della registrazione dei dati, in modo tale da poter effettuare poi il login sulla piattaforma.
					Sia studenti che professori possono entrare a far parte del mondo di Clever College, incrementando la sicurezza
					nel Campus e rispettando così le regole vigenti. Accedi anche tu, ti stiamo aspettando!
				</p>
			</div>
			<div class="row">
				<div class="col-lg-4 col-md-4 col-sm-4 center-object-img">
					<img src="assets/images/user_for_index_1.png">
				</div>
				<div class="col-lg-4 col-md-4 col-sm-4 center-object-img" >
					<img src="assets/images/user_for_index_3.png">
				</div>
				<div class="col-lg-4 col-md-4 col-sm-4 center-object-img">
					<img src="assets/images/user_for_index_2.png">
				</div>
			</div>
		</div>
		<div class="footer"></div>
	</body>
</html>
