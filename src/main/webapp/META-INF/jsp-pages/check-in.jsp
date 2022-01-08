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
<script src="/js/common.js"></script>
<script src="/js/data-model.js"></script>
<script src="/js/check_in.js"></script>

</head>
<body>
	<div class="header"></div>
	<div class="container">
		<div class="video-capture-container">
			<video width="320" height="320" class="video-capture"></video>
			<canvas style="display:none;"></canvas>
		</div>
		<button class="btn btn-outline-primary" id="showStreamBttn">Apri Fotocamera</button>
	</div>
</body>
</html>