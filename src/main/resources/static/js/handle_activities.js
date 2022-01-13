
const weekday = ['lunedì', 'martedì', 'mercoledì', 'giovedì', 'venerdì', 'sabato', 'domenica'];

$(function() {

	loadActivities();
	$(document).on('change', '.custom-control', function(event) {
		console.log(event.target.id);
		disableOrEnableActivityConfirm(event.target.id.slice(9), !event.target.checked);
	});
});

function loadActivities() {
	loadedActivities = [];

	$('.accordion').empty();

	$.ajax({
		type: "get",
		url: "/getWeeklyLessons",
		success: function(activities) {
			if (activities.length === 0)
				loadNoActivitiesCard('#weeklyLessonsAccordion');
			for (let i = 0; i < activities.length; ++i) {
				loadWeeklyCard(activities[i]);
				loadedActivities.push(JSON.stringify(activities[i]));
			}
		},
		error: errorMessage
	});

	$.ajax({
		type: "get",
		url: "/getSingleLessons",
		success: function(activities) {
			if (activities.length === 0)
				loadNoActivitiesCard('#singleLessonsAccordion');
			for (let i = 0; i < activities.length; ++i) {
				loadSingleCard(activities[i]);
				loadedActivities.push(JSON.stringify(activities[i]));
			}

		},
		error: errorMessage
	});

	$.ajax({
		type: "get",
		url: "/getSeminars",
		success: function(activities) {
			if (activities.length === 0)
				loadNoActivitiesCard('#seminarsAccordion');
			for (let i = 0; i < activities.length; ++i) {
				loadSeminarCard(activities[i]);
				loadedActivities.push(JSON.stringify(activities[i]));
			}

		},
		error: errorMessage
	});


}

function loadWeeklyCard(activity) {
	let card = document.createElement('div');
	card.classList.add('card');

	let cardHeader = document.createElement('div');
	cardHeader.classList.add('card-header');
	cardHeader.innerHTML += '<p class="card-title">Lezione settimanale di ' + activity.course.name +
		' di ' + weekday[activity.weekDay] + '</p>';

	let checkboxDiv = document.createElement('div');
	checkboxDiv.classList.add('custom-control');
	checkboxDiv.classList.add('custom-switch');

	let checkboxInput = document.createElement('input');
	checkboxInput.type = 'checkbox';
	checkboxInput.id = 'switchFor' + activity.id;
	checkboxInput.classList.add('custom-control-input');
	checkboxInput.checked = !activity.disabled;


	let checkboxLabel = document.createElement('label');
	checkboxLabel.classList.add('custom-control-label');
	checkboxLabel.htmlFor = 'switchFor' + activity.id;

	let iconDiv = document.createElement('div');
	iconDiv.classList.add('accicon');
	iconDiv.classList.add('collapsed');
	iconDiv.id = 'iconForActivity' + activity.id;


	let icon = document.createElement('i');
	icon.classList.add('fas');
	icon.classList.add('fa-angle-down');
	icon.classList.add('rotate-icon');

	let collapsableActivity = document.createElement('div');
	collapsableActivity.classList.add('collapse');
	collapsableActivity.id = 'collapseActivity' + activity.id;

	let activityBody = document.createElement('div');
	activityBody.classList.add('card-body');

	activityBody.innerHTML =
		'<div class="activity-info">' +
		'<div><i class="far fa-clock"></i><strong>Orario: </strong>' + activity.time.slice(0, 5) + 
		' </div>' +
		'<div><i class="far fa-hourglass"></i><strong>Durata: </strong>' + toHHMM(activity.length) + 
		' </div>' +
		'<div><i class="far fa-map"></i><strong>Aula: </strong>' + activity.classroom.name +
		' </div>'+
		'<div><i class="fas fa-book"></i><strong>Corso: </strong>' + activity.course.name +
		' </div>' +
		'</div>' + 
		'<p class="activity-body-description">' + activity.description + '</p>' +
		'<div class="bottom-activity-body">' +
		'<p class="disabled-activity-info ' + (activity.disabled ? '' : 'hide') +
		'" id="disabledActivity' + activity.id + '">' +
		'La lezione è disattivata</p>' +
		'<p class="enabled-activity-info ' + (activity.disabled ? 'hide' : '') +
		'" id="enabledActivity' + activity.id + '">' +
		'La lezione è attivata</p>' +
		'<form method="post" action="/activities/edit_activity">' + 
		'<input name="type" type="hidden" value="weekly"><input name="id" type="hidden" value="' + activity.id + '">' +
		'<button type="submit" class="fas fa-pen"></button>' +
		'<span onclick="deleteActivity(' + activity.id + ')" class="fas fa-trash clickable"></span></div></form>';

	checkboxDiv.append(checkboxInput);
	checkboxDiv.append(checkboxLabel);

	iconDiv.append(icon);

	cardHeader.append(checkboxDiv);
	cardHeader.append(iconDiv);

	collapsableActivity.append(activityBody);

	card.append(cardHeader);
	card.append(collapsableActivity);
	$("#weeklyLessonsAccordion").append(card);

	$('#iconForActivity' + activity.id).attr('data-toggle', 'collapse');
	$('#iconForActivity' + activity.id).attr('data-target', '#collapseActivity' + activity.id);
	$('#collapseActivity' + activity.id).attr('data-parent', '#weeklyLessonsAccordion');
}

function loadSingleCard(activity) {
	let card = document.createElement('div');
	card.classList.add('card');

	let cardHeader = document.createElement('div');
	cardHeader.classList.add('card-header');
	cardHeader.innerHTML += '<p class="card-title">Lezione di ' + activity.course.name + ' del ' + activity.date + '</p>';

	let iconDiv = document.createElement('div');
	iconDiv.classList.add('accicon');
	iconDiv.classList.add('collapsed');
	iconDiv.id = 'iconForActivity' + activity.id;

	let icon = document.createElement('i');
	icon.classList.add('fas');
	icon.classList.add('fa-angle-down');
	icon.classList.add('rotate-icon');

	let collapsableActivity = document.createElement('div');
	collapsableActivity.classList.add('collapse');
	collapsableActivity.id = 'collapseActivity' + activity.id;

	let activityBody = document.createElement('div');
	activityBody.classList.add('card-body');

	activityBody.innerHTML =
		'<div class="activity-info">' +
		'<div><i class="far fa-clock"></i><strong>Orario: </strong>' + activity.time.slice(0, 5) + 
		' </div>' +
		'<div><i class="far fa-hourglass"></i><strong>Durata: </strong>' + toHHMM(activity.length) + 
		' </div>' +
		'<div><i class="far fa-map"></i><strong>Aula: </strong>' + activity.classroom.name +
		' </div>'+
		'<div><i class="fas fa-book"></i><strong>Corso: </strong>' + activity.course.name +
		' </div>' +
		'</div>' + 
		'<p class="activity-body-description">' + activity.description + '</p>' +		
		'<div class="bottom-activity-body">' +
		'<form method="post" action="/activities/edit_activity">' + 
		'<input name="type" type="hidden" value="single"><input name="id" type="hidden" value="' + activity.id + '">' +
		'<button type="submit" class="fas fa-pen"></button>' +
		'<span onclick="deleteActivity(' + activity.id + ')" class="fas fa-trash clickable"></span></div></form>';

	iconDiv.append(icon);

	cardHeader.append(iconDiv);

	collapsableActivity.append(activityBody);

	card.append(cardHeader);
	card.append(collapsableActivity);
	$("#singleLessonsAccordion").append(card);

	$('#iconForActivity' + activity.id).attr('data-toggle', 'collapse');
	$('#iconForActivity' + activity.id).attr('data-target', '#collapseActivity' + activity.id);
	$('#collapseActivity' + activity.id).attr('data-parent', '#singleLessonsAccordion');
}

function loadSeminarCard(activity) {
	let card = document.createElement('div');
	card.classList.add('card');

	let cardHeader = document.createElement('div');
	cardHeader.classList.add('card-header');
	cardHeader.innerHTML += '<p class="card-title">Seminario del ' + activity.date + '</p>';

	let iconDiv = document.createElement('div');
	iconDiv.classList.add('accicon');
	iconDiv.classList.add('collapsed');
	iconDiv.id = 'iconForActivity' + activity.id;

	let icon = document.createElement('i');
	icon.classList.add('fas');
	icon.classList.add('fa-angle-down');
	icon.classList.add('rotate-icon');

	let collapsableActivity = document.createElement('div');
	collapsableActivity.classList.add('collapse');
	collapsableActivity.id = 'collapseActivity' + activity.id;

	let activityBody = document.createElement('div');
	activityBody.classList.add('card-body');

	activityBody.innerHTML =
		'<div class="activity-info">' +
		'<div><i class="far fa-clock"></i><strong>Orario: </strong>' + activity.time.slice(0, 5) + 
		' </div>' +
		'<div><i class="far fa-hourglass"></i><strong>Durata: </strong>' + toHHMM(activity.length) + 
		' </div>' +
		'<div><i class="far fa-map"></i><strong>Aula: </strong>' + activity.classroom.name +
		' </div>'+
		'</div>' + 
		'<p class="activity-body-description">' + activity.description + '</p>' +
		'<div class="bottom-activity-body">' +
		'<form method="post" action="/activities/edit_activity">' + 
		'<input name="type" type="hidden" value="seminar"><input name = "id" type="hidden" value="' + activity.id + '">' +
		'<button type="submit" class="fas fa-pen"></button>' +
		'<span onclick="deleteActivity(' + activity.id + ')" class="fas fa-trash clickable"></span></div></form>';

	iconDiv.append(icon);

	cardHeader.append(iconDiv);

	collapsableActivity.append(activityBody);

	card.append(cardHeader);
	card.append(collapsableActivity);
	$("#seminarsAccordion").append(card);

	$('#iconForActivity' + activity.id).attr('data-toggle', 'collapse');
	$('#iconForActivity' + activity.id).attr('data-target', '#collapseActivity' + activity.id);
	$('#collapseActivity' + activity.id).attr('data-parent', '#seminarsAccordion');
}

function loadNoActivitiesCard(target) {
	let card = document.createElement('div');
	card.classList.add('no-activities');
	card.innerHTML = 'Non sono presenti attività di questo tipo';

	$(target).append(card);
}

function disableOrEnableActivityConfirm(id, disable) {
	let indefinite = false;
	if (disable) {
		Swal.fire({
			title: 'Disattiva Lezione',
			text: 'Vuoi disattivare la lezione per questa settimana o indefinitivamente?',
			showDenyButton: true,
			showCancelButton: true,
			confirmButtonText: 'Settimana',
			denyButtonText: 'Sempre',
			cancelButtonText: 'Annulla',
			icon: 'question'
		}).then((result) => {
			if (result.isDismissed) {
				document.getElementById('switchFor' + id).checked = true;
				return;
			}
			else if (result.isDenied) {
				indefinite = true;
			}
			disableOrEnableActivity(id, disable, indefinite);

		});
	} else {
		disableOrEnableActivity(id, disable, indefinite);
	}

}

function disableOrEnableActivity(id, disable, indefinite) {
	$('#disabledActivity' + id).removeClass('hide');
	$('#enabledActivity' + id).removeClass('hide');
	if (disable) {
		$('#enabledActivity' + id).addClass('hide');
	}
	else {
		$('#disabledActivity' + id).addClass('hide');
	}
	$.post("/enableWeeklyLesson", {
		id: id,
		disable: disable,
		indefinite: indefinite
	});
}

function toHHMM(length) {
	let minutes = Math.floor(length / 60);
	let seconds = Math.floor(length % 60);
	if (minutes < 10)
		minutes = '0' + minutes;
	if (seconds < 10)
		seconds = '0' + seconds;
	let HHMM = minutes + ':' + seconds;
	return HHMM;
}

function deleteActivity(id) {

	Swal.fire({
		title: 'Cancella Attività',
		text: 'Sei sicuro di voler cancellare questa attività?',
		showCancelButton: 'true',
		confirmButtonText: 'Conferma',
		cancelButtonText: 'Annulla',
		icon: 'warning'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				type: 'post',
				url: '/deleteActivity',
				data: { id: id },
				success: function() {
					Swal.fire({
						title: 'Attività Cancellata',
						text: "L'attività è stata cancellata con successo",
						icon: 'success'
					});
				}
			}).then(function() {loadActivities();});
		}
	});
}

function editActivity(id, type) {
	let activity;
	for (let i = 0; i < loadedActivities.length; ++i) {
		if (loadedActivities[i].id == id) {
			activity = loadedActivities[i];
		}
	}

	$.ajax({
		type: 'post',
		url: 'editActivity',
		data: {
			activityJSON: JSON.stringify(activity),
			type: type
		},
		error: errorMessage
	});
}
