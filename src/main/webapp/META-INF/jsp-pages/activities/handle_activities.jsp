<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>Gestisci attività</title>
<meta charset="UTF-8">
<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width'
	name='viewport'>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/activities.css">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js"></script>
<script src="/js/notification.js" charset="UTF-8"></script>
<script src="/js/common.js"></script>
<script src="/js/generic_error.js" charset="UTF-8"></script>
<script src="/js/data-model.js"></script>
<script src="/js/handle_activities.js"></script>

</head>
<body>
	<div class="header"></div>

	<div class="container" id="activitiesContainer">
		<h2>Lezioni Settimanali</h2>
		<div class="accordion" id="weeklyLessonsAccordion"></div>
		<h2>Lezioni singole</h2>
		<div class="accordion" id="singleLessonsAccordion"></div>
		<h2>Seminari</h2>
		<div class="accordion" id="seminarsAccordion"></div>
		<div class="add-activity">
			<a href="create_activity" class="far fa-calendar-plus"></a>
		</div>
	</div>

	<div class="footer"></div>
</body>
</html>