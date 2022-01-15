<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Area Personale</title>
	<meta charset="ISO-8859-1">
	<meta content="maximum-scale=1.0, initial-scale=1.0, width=device-width" name="viewport">
	
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">
	
	<link rel="stylesheet" href="/css/common.css">
	<link rel="stylesheet" href="/css/myprofile.css">
	
	<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	<script	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script src="/js/common.js"></script>
	<script src="/js/myprofile.js" charset="UTF-8"></script>
	<script src="/js/loadbookedcourses.js"></script>
	<script src="/js/load_calendar.js"></script>
	<script src="/js/generic_error.js" charset="UTF-8"></script>
	
</head>

<body>
	<div class="header"></div>
	<div class="content container-fluid">
		<div id="mainTable" class="row">
			<div class="col-xl-9 col-lg-8 col-md-12 col-sm-12 col-12" id="tableCol" style="margin-top: 10px">
				<div id="booked-tab">
					<h1>Attività prenotate</h1>
					  <div class="table-responsive">
						<table class="table table-fixed table-bordered" id="week-calendar">
							<thead>
								<tr>
									<th></th>
									<th>Lunedì</th>
									<th>Martedì</th>
									<th>Mercoledì</th>
									<th>Giovedì</th>
									<th>Venerdì</th>
									<th>Sabato</th>							
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>	
			<div class="col-xl-3 col-lg-4 col-md-12 col-sm-12 col-12" style="margin-top: 10px">
				<div class="card">
					<div style="text-align: center">
						<img id="profile-picture" class="card-img-top rounded-circle" alt="ma guarda quanto sei bello/a" src="/assets/images/pp-placeholder.png">
						<span id="modPP-icon" class="card-text clickable fas fa-pen"></span>
						<input type="file" id="modPP" accept=".png,.jpg,.jpeg" style="display: none">
					</div>
					<div id="info" class="card-body">
						<p class="card-text">Nome: <span id="name">${user.firstName}</span></p>
						<p class="card-text">Cognome: <span id="surname">${user.lastName}</span></p>
						<c:if test="${user_type == 'student'}">
							<p class="card-text">Matricola: <span id="s-number">${user.studentNumber}</span></p>
						</c:if>
						<p class="card-text">Codice Fiscale: <span id="cf">${user.cf}</span></p>
						<p class="card-text">E-mail: <span id="email">${user.email}</span></p>
						<p class="card-text">
							Descrizione: <span style="font-style: italic; font-weight:normal">(Max: 256 caratteri)</span>
							<span id="modDescription" class="card-text clickable fas fa-pen"></span>
						</p>
						<div id="descriptionLayout">					
							<c:if test="${empty user.description}">
								<label id="dd-placeholder">Non è presente una descrizione. Aggiungine una!</label>
							</c:if> 
							<c:if test="${not empty user.description}">
								<div id="description">${user.description}</div>
							</c:if>
						</div>
						<form id="passwordChangeLayout" style="margin-top:15px">
							<button type="button" id="changePassword" class="btn btn-outline-danger card-link">Cambia password</button>							
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>	
	<div class="footer"></div>
</body>

</html>