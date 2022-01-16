var matrix = [];

$(function() {
	var start = 8;
	for (let i = 0; i < 13 * 4; i++) {
		if (i % 4 == 0) {
			var time = start + i / 4;
			$("#week-calendar").append("<tr id='time-" + time + "'><th class='v-header' rowspan='4'>" + time + ":00</th>");
		}
		else {
			$("#week-calendar").append("<tr></tr>");
			
		}
		$("#week-calendar").append("</tr>");
	}
	
	for (let i = 0; i < 52; i++) {
		matrix[i] = new Array(6);
	}
	
	
	$.ajax({
		type: 'POST',
		url: "loadBookedWeekActivities",
		success: function(data) {
			if (data != null) {
				for(let i = 0; i < data.length; i++)
					addActivity(data[i]);	
				importCalendar(matrix);		
			}
		},
		error: errorMessage
	});
});

var addActivity = function(element) {
	
	var actTime = element.time.split(":");
	var hour = parseInt(actTime[0]);
	var min = parseInt(actTime[1]);
	var sec = parseInt(actTime[2]);
	
	var actDate = new Date(element.date);
	actDate.setHours(hour, min, sec);

	var endTime = new Date(element.date);
	endTime.setHours(hour, min + element.length, sec);
	
	var quarterStart = Math.round(min / 15);
	var span = Math.round(element.length / 15);
	
	var weekDay;
	weekDay = actDate.getDay();
	
	if (weekDay > 0) {
		for (let i = (hour - 8) * 4 + quarterStart; i < (hour - 8) * 4 + quarterStart + span && i < 52 && i >= 0; i++) {
			if (matrix[i][weekDay - 1] == undefined) {
				matrix[i][weekDay - 1] = element;
			}	
		}
	}
}

var importCalendar = function(matrix) {
	
	var table = $("#week-calendar");
	
	var colorIndex = [];
	
	var id = null;
	var n = 0;
	for (let j = 0; j < 6; j++) {
		for (let i = 0; i < 52; i++) {
			var element = matrix[i][j];
			if (id == null || id == undefined) {
				if (element != undefined) {
					id = element;
					n = 1;
				}
				else table.children().eq(i + 1).append("<td></td>");
			}
			else  {
				if (id === element) {
					n++;					
				}
				else if (element == undefined) {
					if (id.course == undefined) id.color = actColors[5];
					else if(colorIndex.indexOf(id.course.name) != -1) {
						id.color = actColors[colorIndex.indexOf(id.course.name) % 5];		
					}
					else {
						colorIndex.push(id.course.name);
						id.color = actColors[colorIndex.indexOf(id.course.name) % 5];
					} 
					addTd(id, i, n, table, );
					id = null;
					n = 0;
					table.children().eq(i + 1).append("<td></td>");
				}
				else {
					if (id.course == undefined) id.color = actColors[5];
					else if(colorIndex.indexOf(id.course.name) != -1) {
						id.color = actColors[colorIndex.indexOf(id.course.name) % 5];		
					}
					else {
						colorIndex.push(id.course.name);
						id.color = actColors[colorIndex.indexOf(id.course.name) % 5];
					} 
					addTd(id, i, n, table);
					id = element;
					n = 1;
				}
			}
		}
	}
}

var addTd = function(id, i, n, table) {
	var timeFormat = new Intl.DateTimeFormat("it", {
		timeStyle: "short",
		hourCycle: "h24"
	});
	
	var actTime = id.time.split(":");
	var hour = parseInt(actTime[0]);
	var min = parseInt(actTime[1]);
	var sec = parseInt(actTime[2]);
	
	var actDate = new Date(id.date);
	actDate.setHours(hour, min, sec);

	var endTime = new Date(id.date);
	endTime.setHours(hour, min + id.length, sec);
	
	
	var rowspan = n;
	if (id.course != undefined) 
		table.children().eq(i+1 - n).append("<td class='act clickable' rowspan='" + rowspan +"'> <p style='font-weight:bold'>" + id.course.name + "</p>");
	else table.children().eq(i+1 - n).append("<td class='act clickable' rowspan='" + rowspan +"'> <p style='font-weight:bold'>Seminario</p>");

	table.children().eq(i+1 - n).children().last().append("<p>" + timeFormat.format(actDate) + " - "  + timeFormat.format(endTime) + "</p>");
	
	table.children().eq(i+1 - n).children().last().append("<i class='fas fa-info-circle' style='float: right; position: absolute; bottom:12px; right:12px; color:#404040'></i>")
		
	table.children().eq(i+1 - n).append("</td>");
	table.children().eq(i+1 - n).children().last().css("text-align", "center");
	table.children().eq(i+1 - n).children().last().css("border", "2px outset " + id.color + ")");
	table.children().eq(i+1 - n).children().last().css("background-color", id.color + ", 30%)");
	table.children().eq(i+1 - n).children().last().css("position", "relative");
	table.children().eq(i+1 - n).children().last().css("vertical-align", "middle");
	table.children().eq(i+1 - n).children().last().on("click", function() { actSwal(id, actDate, endTime, timeFormat) });
}		

var actColors = ["rgb(157, 174, 224", "rgb(161, 198, 98", "rgb(255, 248, 159", "rgb(249, 205, 248", "rgb(205, 186, 238", "rgb(238, 197, 166"];

var actSwal = function(id, start, end, timeFormat) {
	var dateFormat = new Intl.DateTimeFormat("it", {
		dateStyle: "long",
	})
	
	if (id.course != undefined)
		Swal.fire({
			icon: 'info',
			iconColor: '#7e57c2',
			title: "Lezione di " + id.course.name,
			html: "<h1>" + dateFormat.format(start) + "</h2>" +
				  "<h2 style='text-align: left'> Orario: <span style='color: black'>" + timeFormat.format(start) + " - " + timeFormat.format(end) + "</span></h2>" +
				  "<h2 style='text-align: left'> Aula: <span style='color: black'>" + id.classroom.name + "</span></h2>" +
				  "<h2 style='text-align: left'> Professore: <span style='color: black'>" + id.manager.firstName + " " + id.manager.lastName + "</span></h2>" +
				  "<h2 style='text-align: left'> Descrizione: <span style='color: black'>" + id.description + "</span></h2>"
		});
	else 
		Swal.fire({
			icon: 'info',
			iconColor: '#7e57c2',
			title: "Seminario",
			html: "<h1>" + dateFormat.format(start) + "</h2>" +
				  "<h2 style='text-align: left'> Orario: <span style='color: black'>" + timeFormat.format(start) + " - " + timeFormat.format(end) + "</span></h2>" +
				  "<h2 style='text-align: left'> Aula: <span style='color: black'>" + id.classroom.name + "</span></h2>" +
				  "<h2 style='text-align: left'> Professore: <span style='color: black'>" + id.manager.firstName + " " + id.manager.lastName + "</span></h2>" +
				  "<h2 style='text-align: left'> Descrizione: <span style='color: black'>" + id.description + "</span></h2>"
		});
}