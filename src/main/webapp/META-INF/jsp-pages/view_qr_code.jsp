<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Codice QR</title>
</head>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/check_in_check_out.css">

<script src="/js/common.js"></script>

<body>
	<div class="header"></div>
	<div class="container">
		<h2 id="title">Codice QR per ${location_name}</h2>
		<div id="qrCodeContainer">
			<img class="qr-code-image" alt="QR-Code"
				src="/assets/images/locations-qr-codes/location_${location_id}.png">
		</div>
		<div id="downloadContainer">
			<a
			href="/assets/images/locations-qr-codes/location_${location_id}.png"
			class="fas fa-download" download="qr-code-${location_id}"></a></div>
	</div>
	<div class="footer"></div>
</body>
</html>