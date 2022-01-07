var type = "lessons";
var bookedActivities = new Array();
var bookedLessonsNotExpired;
var bookedSeminarsNotExpired;
const weekday = ["Domenica", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"];
const months = ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"];
var errorShown = false;

$(function() {
	
	$("#typeInput").val("lessons");
	
	$("#typeInput").on("change", function() {
		type = $(this).val();
		loadMore(false);
	})
	
	loadActivitiesNotExpired();
})

function loadActivitiesNotExpired() {
	
	$.ajax({
		type: "POST",
		url: "/loadBookedLessonsNotExpired",
		success: function(data) {

			bookedLessonsNotExpired = data;
			
			$.ajax({
				type: "POST",
				url: "/loadBookedSeminarsNotExpired",
				success: function(data2) {
					bookedSeminarsNotExpired = data2;
					
					loadMore(false);
				},
				error: errorMessage
			})	
		},
		error: errorMessage
	})		
}

function loadMore(showMore) {
	
	if(showMore)
		offset = bookedActivities.length;
	else
		offset = 0;
		
	let remType = type;
	
	if(remType == "lessons") {
		$.ajax({
			type: "POST",
			url: "/loadBookedLessons",
			data: {
				offset: offset
			},
			success: function(data) {
				displayBookedActivities(data, remType, showMore);
				setupUnbookButtons();
			},
			error: errorMessage
		})
	}
	else if(remType == "seminars") {

		$.ajax({
			type: "POST",
			url: "/loadBookedSeminars",
			data: {
				offset: offset
			},
			success: function(data) {
				displayBookedActivities(data, remType, showMore);
				setupUnbookButtons();
			},
			error: errorMessage
		})
	}
}

function displayBookedActivities(data, remType, showMore) {
	
	var showMoreButton = $("#showMoreButton");
	if(showMoreButton != null) {
		showMoreButton.remove();
	}
	
	/*if(bookedActivities.length != 0 && data.length != 0){
		if(areEquals(data.slice(0,15), bookedActivities)) {
			if(data.length == 16) {
				$("#bookingsContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
				$("#showMoreButton").off().on("click", function() {
					loadMore(true);
				});
			}
			return;
		}
	}*/
	
	if(!showMore) {
		bookedActivities = new Array();
		$("#bookings").empty();
	}
	
	bookedActivities = bookedActivities.concat(data.slice(0,15));
	if(bookedActivities.length <= 15)
	index = 0;
	
	var bookingsList = $("#bookings");
			
	var bookedActivitiesNotExpired;
	
	if(remType == "lessons") {
		bookedActivitiesNotExpired = bookedLessonsNotExpired;	
	}
	else if(remType == "seminars") {
		bookedActivitiesNotExpired = bookedSeminarsNotExpired;	
	}
	
	for(; index < bookedActivities.length; index++) {

		let date = new Date(bookedActivities[index].date);
		var title;
		var unbookClass = "";
		var expiredClass = "";

		if(remType == "lessons") {
			title = "Lezione di " + bookedActivities[index].course.name + " di " + weekday[date.getDay()] + " " + date.getDate() + " " + months[date.getMonth()];
		}
		else if(remType == "seminars") {
			title = "Seminario di " + weekday[date.getDay()] + " " + date.getDate() + " " + months[date.getMonth()];
		}
		
		if(contains(bookedActivitiesNotExpired, bookedActivities[index])) {
			expiredClass = "hidden";
		}
		else {
			unbookClass = "hidden";
		}

		bookingsList.append("<div class='card'>" +
								"<div class='card-header'>" +
									"<p class='card-title'>" + title + "</p>" +
									"<div class='unbook-button-wrapper " + unbookClass + "' id='unbook-wrapper-" + bookedActivities[index].id + "'><button class='btn btn-outline-primary unbook-button' id='unbook-activity-" + bookedActivities[index].id + "'><span class='unbook-button-text'>DISDICI</span><i class='fas fa-minus-circle'></i></button></div>" +
									"<div class='expired-wrapper " + expiredClass + "'><button class='btn btn-outline-primary expired-button'><span class='expired-text'>SCADUTA</span><i class='fas fa-times'></i></button></div>" +
									"<span class='accicon collapsed clickable expand-button' data-toggle='collapse' data-target='#activity-" + bookedActivities[index].id + "' aria-expanded='true'>" +
										"<i class='fas fa-angle-down rotate-icon' aria-hidden='true'></i>" +
									"</span>" +
									"</div>" +
									"<div class='collapse' id='activity-" + bookedActivities[index].id + "'>" +
										"<div class='card-body'>" +
											"<div class='activity-info'>" +
												"<span class='activity-time'><i class='far fa-clock' aria-hidden='true'></i> <strong>Orario: </strong>" + bookedActivities[index].time + "</span>" +
												"<span class='activity-length'><i class='far fa-hourglass' aria-hidden='true'></i> <strong>Durata: </strong>" + toHHMM(bookedActivities[index].length) + "</span>" +
												"<span class='activity-classroom'><i class='far fa-map' aria-hidden='true'></i><strong>Aula: </strong>" + bookedActivities[index].classroom.name + "</span>" +
											"</div>" +
										"<p>" + bookedActivities[index].description + "</p>" +
									"</div>" +
								"</div>" +
							"</div>");
	}

	if(data.length == 16) {
		$("#bookingsContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
		$("#showMoreButton").off().on("click", function() {
			loadMore(true);
		});
	}
}

function setupUnbookButtons() {
	$(".unbook-button").off().on("click", function() {
		let id = this.id.substr(16);
		unbookActivity(id);
	})
}

function contains(array, activity) {
	for (let i = 0; i < array.length; ++i) {
		if (array[i].id == activity.id)
			return true;
	}

	return false;
}

function toHHMM(timeLength) {

	var hours = Math.floor(timeLength / 60);

	if (hours < 10)
		hours = "0" + hours;

	var minutes = timeLength % 60;

	if (minutes < 10)
		minutes = "0" + minutes;

	return hours + ":" + minutes;
}

function errorMessage() {
	if (!errorShown) {
		errorShown = true;
		Swal.fire({
			title: "Oops...",
			text: "Qualcosa è andato storto.",
			icon: "error"
		}).then((result) => {
			if (result.isConfirmed) {
				location.reload();
			}
		})
	}
}

function unbookActivity(activityId) {
	$.ajax({
		type: "POST",
		url: "/setBookedActivity",
		data: {
			activityId: activityId,
			booked: false
		},
		success: function(data) {
			if (data == "ok") {
				loadMore(false);
			}
			else {
				errorMessage();
			}
		},
		error: errorMessage
	})
}