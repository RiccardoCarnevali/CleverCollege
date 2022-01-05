$(function() {
	$('#createActivityBttn').on('click', createActivity);
	$('#editActivityBttn').on('click', editActivity);
	$('input[name="activity-type"]').change(function() {
		hideUnusedInputs(this.value);
	})
	hideUnusedInputs($('input[name="activity-type"]:checked').val());
	loadLocations();
	loadCourses();
})

function hideUnusedInputs(type) {
	$('div').removeClass('hide');
		if (type == 'single') {
			$('#weekdayInput').addClass('hide');
		} else if (type == 'weekly') {
			$('#dateInput').addClass('hide');
		} else if (type == 'seminar') {
			$('#weekdayInput').addClass('hide');
			$('#courseInput').addClass('hide');
		}
}

function loadLocations() {
	activityType = $('input[name="activityType"]:checked').val();
	if (activityType == 'single' || activityType == 'weekly') {
		$.ajax({
			type: 'get',
			url: '/get_classrooms',
			success: function(classrooms) {
				loadedLocations = classrooms;
				for (let i = 0; i < classrooms.length; ++i) {
					$('#locationSelect').append('<option class="' + i + '">' + classrooms[i].name + '</option>');
				}
			}
		})
	} else {
		$.ajax({
			type: 'get',
			url: '/get_locations',
			success: function(locations) {
				loadedLocations = locations;
				for (let i = 0; i < locations.length; ++i) {
					$('#locationSelect').append('<option value="' + i + '">' + locations[i].name + '</option>');
				}
			}
		});
	}
}

function loadCourses() {
	$.ajax({
		type: 'get',
		url: '/get_courses',
		success: function(courses) {
			loadedCourses = courses;
			for (let i = 0; i < courses.length; ++i) {
				$('#courseSelect').append('<option value="' + i + '">' + courses[i].name + '</option>');
			}
		}
	});
}

function createActivity() {
	$('.empty-form-error').remove();
	if (checkFieldsValidity()) {
		var activity = createJSONActivity();
		$.ajax({
			type: "post",
			url: "/create_activity",
			data: {
				jsonString: activity,
				type: $('input[name="activity-type"]:checked').val()
			},
			success: function() {
				Swal.fire(
					'Attività Inserita!',
					"L'attività è stata inserita correttamente.'",
					'success'
				);
				$('#activityInfoInput input').val('');
				$('textarea').val('');
			}
		});
	} else {
		$('#activityInfoInput input:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		$('textarea:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
	}
}

function editActivity() {
	$('.empty-form-error').remove();
	if (checkFieldsValidity()) {
		$.ajax({
			type: 'post',
			url: '/delete_activity',
			data: {
				id: $('input[name="activity-id"]').val()
			},
			success: createActivity
		})
		window.location.href = "/activities/handle_activities";
	} else {
		$('#activityInfoInput input:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		$('textarea:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
	}
}

function checkFieldsValidity() {
	let inputs = $('.activityInfoInput input:not(.hide)');
	for (let i = 0; i < inputs.length; ++i) {
		if (!inputs[i].val())
			return false;
	}
	if (!$('#descriptionTextArea').val())
		return false;
	return true;
}

function createJSONActivity() {
	let activity;

	let type = $('input[name="activity-type"]:checked').val();
	let description = $('#descriptionTextArea').val();
	let time = $('#activityStartPicker').val() + ':00';
	let length = HHMMToInt($('#activityLengthPicker').val());
	let date = $('#activityDatePicker').val();
	let course = loadedCourses[$('#courseSelect').val()];
	let classroom = loadedLocations[$('#locationSelect').val()];
	let weekday = $('#weekdaySelect').val();
	switch (type) {
		case 'single':
			activity = new SingleLesson(null, time, length, description, null, null, classroom, course, date);
			break;
		case 'weekly':
			activity = new WeeklyLesson(null, time, length, description, null, null, classroom, course, weekday);
			break;
		case 'seminar':
			activity = new Seminar(null, time, length, description, null, null, classroom, date);
			break;
	}
	return JSON.stringify(activity);
}


function HHMMToInt(string) {
	let int = string.split(':');
	return minutes = (+int[0]) * 60 + (+int[1]);
}