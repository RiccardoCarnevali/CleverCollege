<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width' name='viewport'>
		
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
		
		<link rel="stylesheet" href="/css/header.css">
		<link rel="stylesheet" href="/css/view_data.css">
		
		<script src="/js/header.js"></script>
		<script src="/js/load_users.js"></script>
		<script src="/js/data_model.js"></script>
		
		<title>Gestisci dati</title>
	</head>
	
	<body>
	
		<div class="header"></div>
		
		<div class="container-fluid content-row" id="dataContainer">
			<input class="col-lg-4 col-md-6 col-sm-12" type="search" id="searchBar" placeholder="Cerca">
			<div class="form-check form-check-inline">
				<div class="checkbox">
					<input class="form-check-input" type="checkbox" id="allCheckbox">
					<label class="form-check-label" for="allCheckbox">Tutti</label>
				</div>
				<div class="checkbox">
					<input class="form-check-input" type="checkbox" id="studentsCheckbox">
					<label class="form-check-label" for="studentsCheckbox">Studenti</label>
				</div>
				<div class="checkbox">
					<input class="form-check-input" type="checkbox" id="professorsCheckbox">
					<label class="form-check-label" for="professorsCheckbox">Professori</label>
				</div>
				<div class="checkbox">
					<input class="form-check-input" type="checkbox" id="administratorsCheckbox">
					<label class="form-check-label" for="administratorsCheckbox">Amministratori</label>
				</div>
			</div>
			<div class="row" id="userRow">
			</div>
			
		</div>
		
		
	
	</body>
</html>
