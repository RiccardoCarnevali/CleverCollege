<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>Check-In</title>
<meta charset="UTF-8">
<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width'
	name='viewport'>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/check_in_check_out.css">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"
	charset="UTF-8"></script>
<script
	src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"
	charset="UTF-8"></script>
<script src="/js/notification.js" charset="UTF-8"></script>
<script src="/js/common.js"></script>
<script src="/js/generic_error.js" charset="UTF-8"></script>
<script src="/js/data-model.js"></script>
<script src="/js/check_in.js"></script>

</head>
<body>
	<div class="header"></div>
	<c:if test="${checkIn == null}">
		<div class="container">

			<h3 class="title">Check-In</h3>
			<h5>Scannerizza un codice qr</h5>
			<div class="video-capture-container">
				<video width="320" height="320" class="video-capture"></video>
				<canvas style="display:none;" class="video-canvas"></canvas>
			</div>
			<button class="btn btn-outline-primary" id="showStreamBttn">Apri
				Fotocamera</button>
			<div class="manual-check-in-container">
				<h5>In alternativa, seleziona manualmente il luogo in cui
					desideri fare il check-in</h5>
				<div id="locationsContainer">
					<input type="search" id="locationSearchBar" class="form-control" placeholder="Cerca"/>
					<div id="locationList">
						<ul id="locations"></ul>
					</div>
				</div>
			</div>
		</div>
	</c:if>
	<c:if test="${checkIn != null}">
		<div class="container" style="margin-top: 100px; max-width: 70vw;">
			<h3 class="title">Check-Out</h3>
			<p style="text-align: center;">Check-in effettuato in
				${checkIn.location.name} alle ${checkIn.inTime.substring(0,5)}</p>
			<div class="check-in-out-button-container">
				<a id="checkOutBttn" href="do-check-out"
					class="btn btn-outline-primary">Check-Out</a>
			</div>
			<c:if test="${user_type == 'professor'}">
				<div class="check-in-out-button-container">
					<a href="/check-in/studentsCheckedIn" class="btn btn-outline-primary">Elenco presenti in aula</a>
				</div>
			</c:if>
		</div>
	</c:if>

</body>
</html>