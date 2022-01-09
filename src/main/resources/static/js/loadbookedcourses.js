$(function(){
	loadBookedCourses();
})

var activities = new Array();

function loadBookedCourses() {
	$.ajax({
		type: 'POST',
		url: 'loadBookedCourses',
		success: function(data) {
			activities = data;
			if (activities != null) {
				$(".list-group").empty();
				
				for (let i = 0; i < activities.length; i++) {
					$('.list-group').append("<li class='list-group-item activity' id='" + activities[i].id + "'>" +
												"<span class='activity-description'>" + activities[i].description + "</span>" +
													"<div class='icons' style='float:right'>" +
														"<span class='clickable remove-button' id='remove-" + activities[i].id + "'><i class='fas fa-trash'></i></span>" +
													"</div>" +
												"<span class='professor-name' style='display:block'>" + activities[i].manager.firstName + " " + activities[i].manager.lastName + "</span>");
					if (activities[i].date != null) {
						$("#" + activities[i].id).append("<span class='activity-date' style='display:block'>" + activities[i].date + "</span>");
					}
					if (activities[i].weekDay != null) {
						$("#" + activities[i].id).append("<span class='activity-week-day' style='display:block'>" + activities[i].weekDay + "</span>");
					}
					$(".list-group").append("</li>");
				}
			}
		}
	})
}