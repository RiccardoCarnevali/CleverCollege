$(function() {
	$('#createActivityBttn').on('click', createActivity);
	$('#editActivityBttn').on('click', editActivity);
	$('input[name="activity-type"]').change(function() {
		hideUnusedInputs(this.value);
	})
	hideUnusedInputs($('input[name="activity-type"]:checked').val());

	like = '';
	loadedLocations = [];
	
	//check if there is a loaded activity already
	var editActivityLocation = $('.location');
	if(editActivityLocation != null) {
		$("#locationSearchBar").val($('.location-name').html());
	} else {
		loadLocations(false);
	}
	
	$("#locationSearchBar").on("input", function() {
		like = $(this).val();
		loadLocations(false);
	});

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

function loadLocations(showMore) {
	if (showMore)
		offset = loadedLocations.length;
	else
		offset = 0;

	$.ajax({
		type: 'post',
		url: '/getLocations',
		data: {
			like: like,
			offset: offset
		},
		success: function(data) {
			$('#showMoreButton').remove();
			if (areEquals(data.slice(0, 15), loadedLocations) && loadedLocations.length != 0) {
				if (data.length == 16) {
					$("#locationsContainer").append('<button class="btn btn-outline-primary"'
						+ ' id="showMoreButton">Mostra altri</button>');
					$("#showMoreButton").off().on("click", function() {
						loadLocations(true);
					});
				}
				return;
			}

			if (!showMore) {
				loadedLocations = [];
				$("#locations").empty();
			}

			loadedLocations = loadedLocations.concat(data.slice(0, 15));
			if (loadedLocations.length <= 15)
				index = 0;
			
			$('#locationList').css('overflow-y', 'scroll');
			if(loadedLocations.length <= 3) {
				$('#locationList').css('overflow-y', 'hidden');
			}
			
			var locationList = $("#locations");
			for (; index < loadedLocations.length; index++) {
				if(loadedLocations[index])
				locationList.append('<li class="list-group-item location">' +
					'<div class="location-name">' + loadedLocations[index].name + '</div>' +
					'<div class="radio-item">' +
					'<input type="radio" name="location-select" value="' + index +
					'" id="location' + index + '">' +
					'<label for="location' + index + '"></div>');
			}
			if (data.length == 16) {
				$("#locationsContainer").append('<button class="btn btn-outline-primary" id="showMoreButton">Mostra altri</button>');
				$("#showMoreButton").off().on("click", function() {
					loadLocations(true);
				});
			}
		}
	});


}

function loadCourses() {
	$.ajax({
		type: 'get',
		url: '/getProfessorCourses',
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
			url: "/createActivity",
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
		let inputs = $('.activityInfoInput input:not(.hide)');
		for (let i = 0; i < inputs.length; ++i) {
			if (!inputs[i].val())
				inputs[i].after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		}
		$('textarea:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
	}
}

function editActivity() {
	$('.empty-form-error').remove();
	if (checkFieldsValidity()) {
		$.ajax({
			type: 'post',
			url: '/deleteActivity',
			data: {
				id: $('input[name="activity-id"]').val()
			},
			success: createActivity
		})
		window.location.href = "/activities/handle_activities";
	} else {
		let inputs = $('.activityInfoInput input:not(.hide)');
		for (let i = 0; i < inputs.length; ++i) {
			if (!inputs[i].val())
				inputs[i].after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		}
		$('textarea:empty').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		if(!$('input[name="location-select"]:checked').val()) {
			$('#locationsContainer').after('<div class="empty-form-error">Questo campo deve essere compilato</div>');
		}
	}
}

function checkFieldsValidity() {
	let inputs = $('.activityInfoInput input:not(.hide)');
	for (let i = 0; i < inputs.length; ++i) {
		if (!inputs[i].val())
			return false;
	}
	if (!$('#descriptionTextArea').val() || !$('input[name="location-select"]:checked').val())
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
	let classroom = loadedLocations[$('input[name="location-select"]:checked').val()];
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

function areEquals(location1, location2) {
	if (location1.length != location2.length)
		return false;

	for (let i = 0; i < location1.length; i++) {
		if (location1[i].id != location2[i].id)
			return false;
	}

	return true;
}