<%@page import="org.json.JSONObject"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<%@ page import="com.clevercollege.model.SingleLesson" %>
<%@ page import="com.clevercollege.model.WeeklyLesson" %>
<%@ page import="com.clevercollege.model.Seminar" %>
<%@ page import="com.clevercollege.model.Course" %>

<!DOCTYPE html>
<html>
<head>
<title>Crea Attività</title>
<meta charset="UTF-8">
<meta content='maximum-scale=1.0, initial-scale=1.0, width=device-width'
	name='viewport'>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/insert.css">
<link rel="stylesheet" href="/css/activities.css">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="/js/common.js"></script>
<script src="/js/data-model.js"></script>
<script src="/js/create_activity.js"></script>
<script src="/js/generic_error.js" charset="UTF-8"></script>

</head>
<body>
	<fmt:formatNumber type="number" minIntegerDigits="2"
		groupingUsed="false" maxFractionDigits="0" pattern="#"
		value="${activity.length / 60}" var="lengthHour" />
	<fmt:formatNumber type="number" minIntegerDigits="2"
		groupingUsed="false" maxFractionDigits="0" pattern="#"
		value="${activity.length % 60}" var="lengthMinute" />

	<div class="header"></div>
	
	<div class="container" id="createActivityContainer">
		<h3>Inserisci un'attività</h3>
		<div id="pickActivityType">
			<div class="radio-item">
				<input type="radio" id="weeklyLessonBttn" name="activity-type"
					value="weekly"
					<c:if test = "${activity_type == 'weekly'}"> checked="checked"</c:if>>
				<label for="weeklyLessonBttn">Lezione Settimanale</label>
			</div>
			<div class="radio-item">
				<input type="radio" id="singleLessonBttn" name="activity-type"
					value="single"
					<c:if test = "${activity_type == 'single' || activity_type == null}"> checked="checked"</c:if>>
				<label for="singleLessonBttn">Lezione Singola</label>
			</div>
			<div class="radio-item">
				<input type="radio" id="seminarBttn" name="activity-type"
					value="seminar"
					<c:if test = "${activity_type == 'seminar'}"> checked="checked"</c:if>>
				<label for="seminarBttn">Seminario</label>
			</div>
		</div>
		<div id="activityInfoInput">
			<div id="infoContainerRight">
				<div class="form-group" id='dateInput'>
					<label for="activityDatePicker"><strong>Data</strong></label> <input
						type="date" id="activityDatePicker" name="activity-date"
						<c:if test="${activity_type != 'weekly' && activity != null}">value="${activity.date}"</c:if>>
				</div>
				<div class="form-group">
					<label for="activityStartPicker"><strong>Orario</strong></label> <input
						type="time" id="activityStartPicker" step="600"
						name="activity-start"
						<c:if test="${activity != null}">value="${activity.time.substring(0,5)}"</c:if>>
				</div>
				<div class="form-group">
					<label for="activityLengthPicker"><strong>Durata</strong></label> <input
						type="time" id="activityLengthPicker" name="activity-length"
						min="00:00" max="10:00" step="600"
						<c:if test="${activity != null}">value="${lengthHour}:${lengthMinute}"</c:if>>
				</div>
				<div class="form-group" id="courseInput">
					<strong>Corso</strong><select class="form-control"
						id="courseSelect"
						<c:if test="${activity != null}">value="activity.course.name"</c:if>></select>
				</div>
				<div class="form-group hide" id="weekdayInput">
					<strong>Giorno Settimanale</strong> <select class="form-control"
						id="weekdaySelect">
						<option value="0"
							<c:if test="${activity_type == 'weekly' && activity.weekDay == 0}">selected="selected"</c:if>>Lunedì</option>
						<option value="1"
							<c:if test="${activity_type == 'weekly' && activity.weekDay == 1}">selected="selected"</c:if>>Martedì</option>
						<option value="2"
							<c:if test="${activity_type == 'weekly' && activity.weekDay == 2}">selected="selected"</c:if>>Mercoledì</option>
						<option value="3"
							<c:if test="${activity_type == 'weekly' && activity.weekDay == 3}">selected="selected"</c:if>>Giovedì</option>
						<option value="4"
							<c:if test="${activity_type == 'weekly' && activity.weekDay == 4}">selected="selected"</c:if>>Venerdì</option>
					</select>
				</div>
			</div>
			<label for="classroomSearchBar" style="margin-left: 32px;"><strong>classroom:</strong></label>
			<div id="classroomsContainer">
				<input type="search" id="classroomSearchBar" class="form-control" />
				<div id="classroomList">
					<ul id="classrooms">
						<c:if test="${activity != null}">
							<li class="list-group-item classroom"><div
									class="classroom-name">${activity.classroom.name}</div>
								<div class="radio-item">
									<input type="radio" name="classroom-select" value="0"
										id="classroom0" checked="checked"><label
										for="classroom0"></label>
								</div></li>
						</c:if>
					</ul>
				</div>

			</div>
			<div class="form-row" id="descriptionInput">
				<label for="descriptionTextarea"><strong>Descrizione</strong></label>
				<textarea class="form-control" id="descriptionTextArea" rows="3"
					cols="50" maxlength="400" name="activity-description"><c:if
						test="${activity != null}">${activity.description}</c:if></textarea>
			</div>
		</div>
		<c:if test="${activity != null}">
			<input type="hidden" name="activity-id" value="${activity.id}">
			<button class="btn btn-outline-primary" id="editActivityBttn">Modifica
				Attività</button>
		</c:if>
		<c:if test="${activity == null}">
			<button class="btn btn-outline-primary" id="createActivityBttn">Crea
				Attività</button>
		</c:if>
	</div>
	<div class="footer"></div>
</body>
</html>