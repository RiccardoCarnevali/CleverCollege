var editActivityCourse = null;

$(function() {
	$('#createActivityBttn').on('click', function(){createActivity(false)});
	$('#editActivityBttn').on('click', function(){editActivity(false)});
	$('input[name="activity-type"]').change(function() {
		hideUnusedInputs(this.value);
	})
	hideUnusedInputs($('input[name="activity-type"]:checked').val());


	loadedClassrooms = [];

	//check if there is a loaded activity already
	var editActivityClassroom = $('.classroom');
	if (editActivityClassroom != null) {
		like = $('.classroom-name').html();
		$("#classroomSearchBar").val(like);
	}
	
	if (like == null)
		like = '';
	loadClassrooms(false);

	$("#classroomSearchBar").on("input", function() {
		like = $(this).val();
		loadClassrooms(false);
	});

	var courseSelect = $('#courseSelect');
	if(courseSelect)
		editActivityCourse = courseSelect.attr('value');
	
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

function loadClassrooms(showMore) {
	if (showMore)
		offset = loadedClassrooms.length;
	else
		offset = 0;

	$.ajax({
		type: 'post',
		url: '/getClassrooms',
		data: {
			like: like,
			offset: offset
		},
		success: function(data) {
			$('#showMoreButton').remove();
			if (areEquals(data.slice(0, 15), loadedClassrooms) && loadedClassrooms.length != 0) {
				if (data.length == 16) {
					$("#classroomList").append('<button class="btn btn-outline-primary"'
						+ ' id="showMoreButton">Mostra altri</button>');
					$("#showMoreButton").off().on("click", function() {
						loadClassrooms(true);
					});
				}
				return;
			}

			if (!showMore) {
				loadedClassrooms = [];
				$("#classrooms").empty();
			}

			loadedClassrooms = loadedClassrooms.concat(data.slice(0, 15));
			if (loadedClassrooms.length <= 15)
				index = 0;

			$('#classroomList').css('overflow-y', 'scroll');
			if (loadedClassrooms.length <= 3) {
				$('#classroomList').css('overflow-y', 'hidden');
			}

			var classroomList = $("#classrooms");
			for (; index < loadedClassrooms.length; index++) {
				if (loadedClassrooms[index])
					classroomList.append('<li class="list-group-item classroom">' +
						'<div class="classroom-name">' + loadedClassrooms[index].name + '</div>' +
						'<div class="radio-item">' +
						'<input type="radio" name="classroom-select" value="' + index +
						'" id="classroom' + index + '">' +
						'<label for="classroom' + index + '"></div>');
			}
			if (data.length == 16) {
				$("#classroomList").append('<button class="btn btn-outline-primary" id="showMoreButton">Mostra altri</button>');
				$("#showMoreButton").off().on("click", function() {
					loadClassrooms(true);
				});
			}
		},
		error: errorMessage
	});


}

function loadCourses() {
	$.ajax({
		type: 'get',
		url: '/getProfessorCourses',
		success: function(courses) {
			loadedCourses = courses;
			
			for (let i = 0; i < courses.length; ++i) {
				if(courses[i].name != editActivityCourse)
					$('#courseSelect').append('<option value="' + i + '">' + courses[i].name + '</option>');
				else
					$('#courseSelect').append('<option value="' + i + '" selected="selected">' + courses[i].name + '</option>');
			}
		
		},
		error: errorMessage
	});
}

function createActivity(ignoreConflict) {
	$('.empty-form-error').remove();
	if (checkFieldsValidity()) {
		var activity = createJSONActivity();

		$.ajax({
			type: "post",
			url: "/createActivity",
			data: {
				jsonString: activity,
				type: $('input[name="activity-type"]:checked').val(),
				edit: false,
				ignoreConflict: ignoreConflict
			},
			success: function(data) {
				if (data == '') {
					Swal.fire(
						'Attività Inserita!',
						"L'attività è stata inserita correttamente.'",
						'success'
					);
					$('#activityInfoInput input').val('');
					$('textarea').val('');
				} else {
					Swal.fire({
						title: 'Conflitto attività!',
						text: conflictAlertText(JSON.parse(data)),
						icon: 'warning',
						showCancelButton: true,
						confirmButtonText: 'Inserisci',
						cancelButtonText: 'Annulla'
					}).then((choice) => {
						if(choice.isConfirmed)
							createActivity(true);
					});
				}
			},
			error: errorMessage
		});
	} else {
		let inputs = $('.activityInfoInput input:not(.hide)');
		for (let i = 0; i < inputs.length; ++i) {
			if (!inputs[i].val())
				inputs[i].after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		}

		if (!$('input[name="classroom-select"]:checked').length)
			$('#classroomsContainer').after('<div class="empty-form-error">Seleziona un\'aula</div>');

		$('textarea:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
	}
}

function editActivity(ignoreConflict) {
	$('.empty-form-error').remove();
	if (checkFieldsValidity()) {
		var activity = createJSONActivity();
		$.ajax({
			type: "post",
			url: "/createActivity",
			data: {
				jsonString: activity,
				type: $('input[name="activity-type"]:checked').val(),
				edit: true,
				ignoreConflict: ignoreConflict
			},
			success: function(data) {
				if (data == '') {
					Swal.fire({
						title: 'Attività Modificata!',
						text: "L'attività è stata modificata con successo.'",
						icon: 'success'
					}).then(function() {
						window.location.href = "handle_activities";
					});
				} else {
					Swal.fire({
						title: 'Conflitto attività!',
						text: conflictAlertText(JSON.parse(data)),
						icon: 'warning',
						showCancelButton: true,
						confirmButtonText: 'Inserisci',
						cancelButtonText: 'Annulla'
					}).then((choice) => {
						if(choice.isConfirmed)
							editActivity(true);
					});
				}
			},
			error: errorMessage
		});
	} else {
		let inputs = $('.activityInfoInput input:not(.hide)');
		for (let i = 0; i < inputs.length; ++i) {
			if (!inputs[i].val())
				inputs[i].after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		}
		if (!$('input[name="classroom-select"]:checked').length)
			$('#classroomsContainer').after('<div class="empty-form-error">Seleziona un\'aula</div>');
		$('textarea:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
	}
}

function conflictAlertText(result) {
	let activityConflict = result.activity_conflict;
	let activityConflictType = activityConflict.activity_conflict_type;
	let text = '';
	let weekdays = ['lunedì', 'martedì', 'mercoledì', 'giovedì', 'venerdì', 'sabato', 'domenica'];
	if (activityConflictType == 'weekly') {
		text = "L'attività che si sta cercando di inserire va in conflitto con la lezione settimanale di "
			+ weekdays[activityConflict.weekDay] + " di " + activityConflict.course.name +
			". Vuoi procedere con l'inserimento?";
	} else if (activityConflictType == 'single') {
		text = "L'attività che si sta cercando di inserire va in conflitto con la lezione del "
			+ activityConflict.date + " di " + activityConflict.course.name +
			". Vuoi procedere con l'inserimento?";
	} else if (activityConflictType == 'seminar') {
		text = "L'attività che si sta cercando di inserire va in conflitto con il seminario del "
			+ activityConflict.date +
			". Vuoi procedere con l'inserimento?";
	}
	return text;
}

function checkFieldsValidity() {
	let inputs = $('.activityInfoInput input:not(.hide)');
	for (let i = 0; i < inputs.length; ++i) {
		if (!inputs[i].val())
			return false;
	}
	if (!$('#descriptionTextArea').val() || !$('input[name="classroom-select"]:checked').val())
		return false;
	return true;
}

function createJSONActivity() {
	let activity;

	let id = $('input[name="activity-id"]').val();
	let type = $('input[name="activity-type"]:checked').val();
	let description = $('#descriptionTextArea').val();
	let time = $('#activityStartPicker').val() + ':00';
	let length = HHMMToInt($('#activityLengthPicker').val());
	let date = $('#activityDatePicker').val();
	let course = loadedCourses[$('#courseSelect').val()];
	let classroom = loadedClassrooms[$('input[name="classroom-select"]:checked').val()];
	let weekday = $('#weekdaySelect').val();
	switch (type) {
		case 'single':
			activity = new SingleLesson(id, time, length, description, null, null, classroom, course, date);
			break;
		case 'weekly':
			activity = new WeeklyLesson(id, time, length, description, null, null, classroom, course, weekday);
			break;
		case 'seminar':
			activity = new Seminar(id, time, length, description, null, null, classroom, date);
			break;
	}
	return JSON.stringify(activity);
}

function HHMMToInt(string) {
	let int = string.split(':');
	return minutes = (+int[0]) * 60 + (+int[1]);
}

function areEquals(classroom1, classroom2) {
	if (classroom1.length != classroom2.length)
		return false;

	for (let i = 0; i < classroom1.length; i++) {
		if (classroom1[i].id != classroom2[i].id)
			return false;
	}

	return true;
}

