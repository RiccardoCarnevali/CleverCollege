var students, professors, administrators;

$(function(){	
	$.ajax({
		type: "GET",
		url: "loadStudents",
		success: function(data) {
			students = data;
			changeCurrentUsers();
		}
	})
		
	$.ajax({
		type: "GET",
		url: "loadProfessors",
		success: function(data) {
			professors = data;
			changeCurrentUsers();
		}
	})
	
	$.ajax({
		type: "GET",
		url: "loadAdmins",
		success: function(data) {
			administrators = data;
			changeCurrentUsers();
		}
	})
	
	setupAutocomplete();
	
	$(window).on("resize", function(){
		let classOnOff = window.matchMedia('(min-width: 450px)').matches;
		$(".form-check").toggleClass("form-check-inline", classOnOff);
	});
	
	$(".not-all").on("change", function(){
		if($(".not-all:checked").length == $(".not-all").length)
			$("#allCheckbox").prop("checked", true);
		else
			$("#allCheckbox").prop("checked", false);
		changeCurrentUsers();
	})
	
	$("#allCheckbox").on("change", function(){
		$(".not-all").prop("checked", this.checked);
		changeCurrentUsers();
	})
})

function loadMoreUsers(usersToDisplay) {
	
	shown += 6;

	for(; index < shown && index < usersToDisplay.length; index++) {
		$("#userRow").append("<div class=\"col-lg-2 col-md-4 col-sm-12 d-flex align-items-stretch\">" +
										"<div class=\"card\">" +
											//"<img class=\"card-img-top\" src=\"" + usersToDisplay[index].profilePicture + "\" alt=\"Card image\">" +
											"<img class=\"card-img-top\" src=\"assets/images/img_avatar1.png\" alt=\"Card image\">" +
												"<div class=\"card-body d-flex flex-column\">" +
													"<h4 class=\"card-title\">" + usersToDisplay[index].firstName + " " + usersToDisplay[index].lastName + "</h4>" +
										    		"<p class=\"card-text\">" + usersToDisplay[index].cf + "</p>" +
										    		"<a href=\"#\" class=\"btn btn-primary mt-auto align-self-start\">See Profile</a>" +
										 		"</div>" +
										"</div>" +
									"</div>");
	}
	
	showMoreButton = $("#showMoreButton");
	if(showMoreButton != null)
		showMoreButton.remove();
		
	if(index < usersToDisplay.length) {
		$("#dataContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
		$("#showMoreButton").on("click", loadMoreUsers);
	}
}

function loadUsers(users) {
	index = 0;
	shown = 0;
	
	$("#userRow").empty();
	
	loadMoreUsers(users);
}

function setupAutocomplete() {
	var searchBar = $("#searchBar");

	searchBar.on("input", function(){
		
		var val = this.value;
		var matchingUsers = new Array();
					
		for(let i = 0; i < currentUsers.length; i++) {
			let name = currentUsers[i].firstName + " " + currentUsers[i].lastName;
			if(name.substr(0, val.length).toUpperCase() == val.toUpperCase()) {
				matchingUsers.push(currentUsers[i]);
			}
		}
		if(typeof(this.previousMatchingUsers) == "undefined" || this.previousMatchingUsers.length != matchingUsers.length )
			loadUsers(matchingUsers);
		this.previousMatchingUsers = matchingUsers;
	});
}

function changeCurrentUsers() {
	if(students == null || professors == null || administrators == null)
		return;
		
	if($("#allCheckbox").is(":checked")) {
		currentUsers = students.concat(professors).concat(administrators);
	}
	else {
		let combinedUsers = new Array();
		
		if($("#studentsCheckbox").is(":checked")) {
			combinedUsers = combinedUsers.concat(students);
		}
		
		if($("#professorsCheckbox").is(":checked")) {
			combinedUsers = combinedUsers.concat(professors);
		}
		
		if($("#administratorsCheckbox").is(":checked")) {
			combinedUsers = combinedUsers.concat(administrators);
		}
		
		currentUsers = combinedUsers;
	}
	loadUsers(currentUsers);
}