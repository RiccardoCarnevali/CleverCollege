var like = "";
var type = "all";
var courses = new Array();
var followed = new Array();

const weekday = ["Domenica", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"];
const months = ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"];

$(function() {
	
	$("#searchBar").val("");
	$("#typeInput").val("all");
	
	$("#searchBar").on("input", function() {
		like = $(this).val();
		loadMore(false);
	});
	
	$("#typeInput").on("change", function() {
		type = $(this).val();
		courses = new Array();
		followed = new Array();
		$("#rows").empty();
		loadMore(false);
	});
	
	loadMore(false);

})

function loadMore(showMore) {
	
	if(showMore)
		offset = courses.length;
	else
		offset = 0;
	
	$.ajax({
		type: "POST",
		url: "/loadCourses",
		data: {
			like: like,
			type: type,
			offset: offset
		},
		success: function(data) {

			var showMoreButton = $("#showMoreButton");
			
			if(showMoreButton != null) {
				showMoreButton.remove();
			}

			if(data.length === 0 && offset === 0) {
				var coursesList = $("#courses");
				coursesList.empty();
				courses = [];
				var message = "";
				if(like !== "") {
					message = "Nessun risultato soddisfa la ricerca.";
				}
				else
					message = "Nessuna lezione è stata ancora registrata.";
				coursesList.append("<li class='list-group-item' style='text-align: center; margin: 10px 0px'>" + message + "</li>");
				return;
			}

			if(courses.length != 0 && data.length != 0){
				if(areEquals(data.slice(0,15), courses)) {
					if(data.length == 16) {
						$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
						$("#showMoreButton").off().on("click", function() {
							loadMore(true);
						});
					}
					return;
				}
			}
			
			if(!showMore) {
				followed = new Array();
				courses = new Array();
				$("#courses").empty();
			}
						
			courses = courses.concat(data.slice(0,15));
			if(courses.length <= 15)
				index = 0;
			
			checkFollowedCourses(data);
		},
		error: errorMessage 
	});
}

function checkFollowedCourses(data) {
	if(type == "all") {
		$.ajax({
			type: "POST",
			url: "/checkFollowed",
			contentType: "application/json",
			data: JSON.stringify(courses.slice(offset, offset + 15)),
			success: function(data2) {
				
				followed = followed.concat(data2);
				displayCourses();
				
				if(data.length == 16) {
					$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
					$("#showMoreButton").off().on("click", function() {
						loadMore(true);
					});
				}
			},
			error: errorMessage
		})
	}
	else {
		for(let i = 0; i < courses.length; i++) {
			followed.push(true);
		}
		
		displayCourses();
		if(data.length == 16) {
			$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
			$("#showMoreButton").off().on("click", function() {
				loadMore(true);
			});
		}
	}
}

function displayCourses() {
	
	var coursesList = $("#courses");
	
	start = index;
	
	for(; index < courses.length; index++) {
		
		var iconClass;
		
		if(followed[index]) {
			iconClass = "fas fa-heart";
		}
		else {
			iconClass = "far fa-heart";
		}
		
		coursesList.append(	"	<li class='list-group-item course'>" +
									"<div class='course-body'>" +
										"<span class='course-info'>" +
											"<span class='course-name'>" + courses[index].name + "</span>" +
											"<span class='professor-name'>" + courses[index].lecturer.firstName + " " + courses[index].lecturer.lastName + "</span>" +
										"</span>" +	
										"<span class='icons'>" +
											"<span class='clickable add-favorite-button' id='add-favorite-" + courses[index].id + "'><i class='" + iconClass + "'></i></span>" +
											"<span class='accicon collapsed clickable expand-button' data-toggle='collapse' data-target='#lessons-for-" + courses[index].id + "'><i class='fas fa-angle-down rotate-icon'></i></a>" +
										"</span>" +
									"</div>" +
									"<div class='collapse lessons-container' id='lessons-for-" + courses[index].id + "'><div class='spinner-border'></div></div>" +
								"</li>");
	}
	
	for(; start < courses.length; start++) {
		
		let c_start = start;
		
		loadLessonsFor(courses[c_start].id)
	}

	$(".add-favorite-button").off().on("click", function() {

		var icon = this.childNodes[0];
		var id = this.id.substr(13);
		
		if(icon.classList.contains("far")) {
			$.ajax({
				type: "POST",
				url: "/setFollowedCourse",
				data: {
					courseId: id,
					followed: true
				},
				success: function() {
					icon.classList.remove("far")
					icon.classList.add("fas");	
				},
				error: errorMessage
			})
		}
		else {
			$.ajax({
				type: "POST",
				url: "/setFollowedCourse",
				data: {
					courseId: id,
					followed: false
				},
				success: function(data) {
					
					if(data == "ok") {
						icon.classList.remove("fas")
						icon.classList.add("far");
					} 
					else {
						errorMessage();
					}
				},
				error: errorMessage
			})
		}
	})
}

function areEquals(courses1, courses2) {
	if(courses1.length != courses2.length)
		return false;
		
	for(let i = 0; i < courses1.length; i++) {
		if(courses1[i].id != courses2[i].id)
			return false;
	}
	
	return true;
}

function toHHMM(timeLength) {

	var hours = Math.floor(timeLength / 60);
	
	if(hours < 10)
		hours = "0" + hours;
		
	var minutes = timeLength % 60;
	
	if(minutes < 10)
		minutes = "0" + minutes;
		
	return hours + ":" + minutes;
}

function contains(array, lesson) {
	for(let i = 0; i < array.length; ++i) {
		if(array[i].id == lesson.id)
			return true;
	}
	
	return false;
}

function loadLessonsFor(courseId) {
	
	$.ajax({
	type: "POST",
	url: "/loadLessonsForCourse",
	data: {
		courseId: courseId
	},
	success: function(lessons) {
		checkBookedLessonsFor(lessons, courseId);
	},
	error: errorMessage
})
}

function checkBookedLessonsFor(lessons, courseId) {
	$.ajax({
		type: "POST",
		url: "/loadBookedLessonsNotExpired",
		success: function(bookedLessons) {
			
			displayLessonsFor(lessons, bookedLessons, courseId);
			setupBookButtons();
			setupUnbookButtons();
		},
		error: errorMessage
	})
}

function displayLessonsFor(lessons, bookedLessons, courseId) {
	var lessonsHTML = "";
			
	if(lessons.length == 0)
		lessonsHTML = lessonsHTML.concat("<div class='no-activities' style='box-shadow:none'>Non sono in programma lezioni per questo corso</div>")
	
	for(let i = 0; i < lessons.length; i++) {
		
		if(contains(bookedLessons,lessons[i])) {
			bookClass = "hidden";
			unBookClass = "";
		}
		else {
			bookClass = "";
			unBookClass = "hidden";
		}
		
		let date = new Date(lessons[i].date);
		
		lessonsHTML = lessonsHTML.concat("	<div class='card'>" +
										        "<div class='card-header'>" +
 										            "<p class='card-title'>Lezione di " + weekday[date.getDay()] + " " + date.getDate() + " " + months[date.getMonth()] + "</p>" +
													"<div class='unbook-button-wrapper " + unBookClass + "' id='unbook-wrapper-" + lessons[i].id + "'><button class='btn btn-outline-primary unbook-button' id='unbook-lesson-" + lessons[i].id + "'><span class='unbook-button-text'>DISDICI</span><i class='fas fa-minus-circle'></i></button></div>" +
													"<div class='book-button-wrapper " + bookClass + "' id='book-wrapper-" + lessons[i].id + "'><button class='btn btn-outline-primary book-button' id='book-lesson-" + lessons[i].id + "'><span class='book-button-text'>PRENOTA</span><i class='fas fa-plus-circle'></i></button></div>" +
										            "<span class='accicon collapsed clickable expand-button' data-toggle='collapse' data-target='#lesson-" + lessons[i].id + "' aria-expanded='true'>" +
										                "<i class='fas fa-angle-down rotate-icon' aria-hidden='true'></i>" +
										            "</span>" +
										        "</div>" +
										        "<div class='collapse' id='lesson-" + lessons[i].id + "'>" +
										            "<div class='card-body'>" +
														"<div class='activity-info'>" +
														        "<span class='activity-time'><i class='far fa-clock' aria-hidden='true'></i> <strong>Orario: </strong>" + lessons[i].time + "</span>" +
														        "<span class='activity-length'><i class='far fa-hourglass' aria-hidden='true'></i> <strong>Durata: </strong>" + toHHMM(lessons[i].length) + "</span>" +
														    	"<span class='activity-classroom'><i class='far fa-map' aria-hidden='true'></i><strong>Aula: </strong>" + lessons[i].classroom.name + "</span>" +
														"</div>" +
														"<p>" + lessons[i].description + "</p>" +
										        	"</div>" +
										        "</div>" +
										    "</div>");
	}
	
	jLessonsContainer = $("#lessons-for-" + courseId);
	jLessonsContainer.empty();
	jLessonsContainer.append(lessonsHTML);
}

function setupBookButtons() {
	$(".book-button").off().on("click", function() {
	
		let id = this.id.substr(12);
		
		$.ajax({
			type: "POST",
			url: "/checkCollidingActivities",
			data: {
				activityId: id
			},
			success: function(data) {
				
				if(data) {
					Swal.fire({
						title: "Attenzione",
						text: "Questa attività sarà svolta in un arco di tempo per il quale hai già prenotato un'altra attività, sei sicuro di volere prenotare?'",
						icon: "warning",
						confirmButtonText: "Continua",
						showCancelButton: true,
						cancelButtonText: "Cancella"
					}).then((result) => {
							if (result.isConfirmed) {
								bookLesson(id);
							}
						});
				}
				else {
					bookLesson(id);
				}
			},
			error: errorMessage
		})
	})
}

function setupUnbookButtons() {
	$(".unbook-button").off().on("click", function() {
		let id = this.id.substr(14);
		unbookLesson(id);
	})
}

function bookLesson(lessonId) {
	$.ajax({
	type: "POST",
	url: "/setBookedActivity",
	data: {
		activityId: lessonId,
		booked: true
	},
	success: function(data) {
		
		if(data == "ok") {
			$("#book-wrapper-" + lessonId).addClass("hidden");
			$("#unbook-wrapper-" + lessonId).removeClass("hidden");
		}
		else if(data == "error") {
			errorMessage();
		}
		else if(data == "maximum capacity") {a
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

function unbookLesson(lessonId) {
	$.ajax({
		type: "POST",
		url: "/setBookedActivity",
		data: {
			activityId: lessonId,
			booked: false
		},
		success: function(data) {
			if(data == "ok") {
				$("#unbook-wrapper-" + lessonId).addClass("hidden");
				$("#book-wrapper-" + lessonId).removeClass("hidden");
			}
			else {
				errorMessage();
			}
		},
		error: errorMessage
	})
}