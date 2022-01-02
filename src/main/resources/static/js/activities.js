/**
 * 
 */

const weekday = ['lunedì', 'martedì', 'mercoledì', 'giovedì', 'venerdì', 'sabato', 'domenica'];

$(function() {
	loadActivities();
	$(document).on('change', '.custom-control', function(event) {
		disableOrEnableActivityConfirm(event.target.id.slice(-1), !event.target.checked);
	});
})

function loadActivities() {
	$.ajax({
		async: false,
		type: "get",
		url: "/view_weekly_activities",
		success: function(activities) {
			for (let i = 0; i < activities.length; ++i) {
				loadWeeklyCard(activities[i]);
			}
		}
	})

	$.ajax({
		async: false,
		type: "get",
		url: "/view_single_activities",
		success: function(activities) {
			for (let i = 0; i < activities.length; ++i) {
				$("#singleActivitiesTable").append('<tr>' +
					'<td">' + activities[i].description + '</td>' +
					'<td>' + activities[i].date + '</th>' +
					'<td>' + activities[i].time + '</th>' +
					'<td>' + toHHMM(activities[i].length) + '</th>' +
					'<td>' + activities[i].classroom.name + '</th>' +
					'</tr>');
			}
			
		}
	})
	loadCreateActivity();
	
}

function loadWeeklyCard(activity) {
	let card = document.createElement('div');
	card.classList.add('card');

	let cardHeader = document.createElement('div');
	cardHeader.classList.add('card-header');
	cardHeader.innerHTML += '<p class="card-title">Lezione settimanale di ' + activity.course.name +
		' di ' + weekday[activity.weekDay] +'</p>';

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
		'<i class="far fa-clock"></i>' +
		' <strong>Orario:</strong> ' + activity.time.slice(0,5) + '</p>' +
		'<i class="far fa-hourglass"></i>' +
		' <strong>Durata:</strong> ' + toHHMM(activity.length) + '</p>' +
		'</div>' +
		activity.description +
		'<p class="disabled-activity-info ' + (activity.disabled ? '' : 'hide') +
		'" id="disabledActivity' + activity.id + '">' +
		"La lezione è disattivata" +
		'</p>' +
		'<p class="enabled-activity-info ' + (activity.disabled ? 'hide' : '') +
		'" id="enabledActivity' + activity.id + '">' +
		"La lezione è attivata" +
		'</p>';

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

function loadCreateActivity() {
	let createActivity = document.createElement('div');
	createActivity.innerHTML = '<div class="add-activity"><a href="create_activity" class="far fa-calendar-plus"></a></div>';
	
	$("#weeklyLessonsAccordion").append(createActivity);
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
	$.post("/enable_weekly_lesson", {
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


