<!DOCTYPE html>
<html>
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
		<link rel="stylesheet" href="/css/view_data.css">
		
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"></script>
		<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"></script>
		<script src="/js/notification.js" charset="UTF-8"></script>
		<script src="/js/common.js"></script>
		<script src="/js/generic_error.js" charset="UTF-8"></script>
		<script src="/js/load_courses.js" charset="UTF-8"></script>
				
		<title>Corsi</title>
	</head>
	
	<body>
	
		<div class="header"></div>
		
		<div class="container-fluid" id="coursesContainer">
		
			<div class="row justify-content-md-right">
				<a href="/courses/insert" class="btn btn-outline-primary col-lg-2 col-md-2 col-sm-4 col-6" id="addButton">Aggiungi</a>
			</div>
		
			<span id="label">Corsi</span>
			<input class="form-control col-lg-4 col-md-6 col-sm-11 col-11" type="search" id="searchBar" placeholder="Cerca"><i class="fas fa-search" id="search-icon"></i>
			
			<ul class="list-group" id="courses">	  
			</ul>
					
		</div>
	
		<div class="footer"></div>
	
	</body>
</html>
