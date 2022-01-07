const weekday = ["Domenica", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"];
const months = ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"];

$(function() {
	$.ajax({
		type: "POST",
		url: "/loadSeminars",
		success: function(data) {
			checkBookedSeminars(data);
		},
		error: errorMessage
	})
})

function checkBookedSeminars(seminars) {
	$.ajax({
		type: "POST",
		url: "/loadBookedSeminarsNotExpired",
		success: function(bookedSeminars) {

			displaySeminars(seminars, bookedSeminars);
			setupBookButtons();
			setupUnbookButtons();
		},
		error: errorMessage
	})
}

function displaySeminars(seminars, bookedSeminars) {

	var seminarsList = $("#seminars");

	if (seminars.length == 0)
		seminarsList.append("<div class='no-activities' style='box-shadow:none'>Non sono in programma seminari</div>")

	for (let i = 0; i < seminars.length; i++) {

		if (contains(bookedSeminars, seminars[i])) {
			bookClass = "hidden";
			unBookClass = "";
		}
		else {
			bookClass = "";
			unBookClass = "hidden";
		}

		let date = new Date(seminars[i].date);

		seminarsList.append("<div class='card'>" +
								"<div class='card-header'>" +
									"<p class='card-title'>Seminario di " + weekday[date.getDay()] + " " + date.getDate() + " " + months[date.getMonth()] + "</p>" +
									"<div class='unbook-button-wrapper " + unBookClass + "' id='unbook-wrapper-" + seminars[i].id + "'><button class='btn btn-outline-primary unbook-button' id='unbook-seminar-" + seminars[i].id + "'><span class='unbook-button-text'>DISDICI</span><i class='fas fa-minus-circle'></i></button></div>" +
									"<div class='book-button-wrapper " + bookClass + "' id='book-wrapper-" + seminars[i].id + "'><button class='btn btn-outline-primary book-button' id='book-seminar-" + seminars[i].id + "'><span class='book-button-text'>PRENOTA</span><i class='fas fa-plus-circle'></i></button></div>" +
									"<span class='accicon collapsed clickable expand-button' data-toggle='collapse' data-target='#seminar-" + seminars[i].id + "' aria-expanded='true'>" +
										"<i class='fas fa-angle-down rotate-icon' aria-hidden='true'></i>" +
									"</span>" +
									"</div>" +
									"<div class='collapse' id='seminar-" + seminars[i].id + "'>" +
										"<div class='card-body'>" +
											"<div class='activity-info'>" +
												"<span class='activity-time'><i class='far fa-clock' aria-hidden='true'></i> <strong>Orario: </strong>" + seminars[i].time + "</span>" +
												"<span class='activity-length'><i class='far fa-hourglass' aria-hidden='true'></i> <strong>Durata: </strong>" + toHHMM(seminars[i].length) + "</span>" +
												"<span class='activity-classroom'><i class='far fa-map' aria-hidden='true'></i><strong>Aula: </strong>" + seminars[i].classroom.name + "</span>" +
											"</div>" +
										"<p>" + seminars[i].description + "</p>" +
									"</div>" +
								"</div>" +
							"</div>");
	}
}

function setupBookButtons() {
	$(".book-button").off().on("click", function() {

		let id = this.id.substr(13);

		$.ajax({
			type: "POST",
			url: "/checkCollidingActivities",
			data: {
				activityId: id
			},
			success: function(data) {

				if (data) {
					Swal.fire({
						title: "Attenzione",
						text: "Questa attività sarà svolta in un arco di tempo per il quale hai già prenotato un'altra attività, sei sicuro di volere prenotare?'",
						icon: "warning",
						confirmButtonText: "Continua",
						showCancelButton: true,
						cancelButtonText: "Cancella"
					}).then((result) => {
						if (result.isConfirmed) {
							bookSeminar(id);
						}
					});
				}
				else {
					bookSeminar(id);
				}
			},
			error: errorMessage
		})
	})
}

function setupUnbookButtons() {
	$(".unbook-button").off().on("click", function() {
		let id = this.id.substr(15);
		unbookSeminar(id);
	})
}

function bookSeminar(seminarId) {
	$.ajax({
		type: "POST",
		url: "/setBookedActivity",
		data: {
			activityId: seminarId,
			booked: true
		},
		success: function(data) {

			if (data == "ok") {
				$("#book-wrapper-" + seminarId).addClass("hidden");
				$("#unbook-wrapper-" + seminarId).removeClass("hidden");
			}
			else if (data == "error") {
				errorMessage();
			}
			else if (data == "maximum capacity") {
			Swal.fire({
				title: "Impossibile effettuare la prenotazione",
				text: "L'aula in cui si svolge l'attività ha raggiunto la capienza massima di prenotazioni",
					icon: "error"
			})
		}
	},
		error: errorMessage
	})
}

function unbookSeminar(seminarId) {
	$.ajax({
		type: "POST",
		url: "/setBookedActivity",
		data: {
			activityId: seminarId,
			booked: false
		},
		success: function(data) {
			if (data == "ok") {
				$("#unbook-wrapper-" + seminarId).addClass("hidden");
				$("#book-wrapper-" + seminarId).removeClass("hidden");
			}
			else {
				errorMessage();
			}
		},
		error: errorMessage
	})
}

function contains(array, seminar) {
	for (let i = 0; i < array.length; ++i) {
		if (array[i].id == seminar.id)
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