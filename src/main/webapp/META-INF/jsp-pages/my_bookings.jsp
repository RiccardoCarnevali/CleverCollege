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
		<link rel="stylesheet" href="/css/book_activities.css">
		
		<script src="/js/common.js"></script>
		<script src="/js/generic_error.js" charset="UTF-8"></script>
		<script src="/js/load_bookings.js" charset="UTF-8"></script>
				
		<title>Le mie prenotazioni</title>
	</head>
	
	<body>
	
		<div class="header"></div>
		
		<div class="container-fluid" id="bookingsContainer">
		
			<span id="label">Prenotazioni</span>
						    
			<select class="form-control col-lg-2 col-md-6 col-sm-12" id="typeInput">
			      <option selected value="lessons">Lezioni</option>
			      <option value="seminars">Seminari</option>
		    </select>
						    
			<ul class="list-group" id="bookings">  
			</ul>
					
		</div>
	
		<div class="footer"></div>
	
	</body>
</html>