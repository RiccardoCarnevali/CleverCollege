$(function(){
	
	$.ajax({
		type: "GET",
		url: "loadUsers",
		success: function(data){
			allUsers = data;
			
			loadUsers(allUsers);
			
			var names = new Array();
			
			for(let j = 0; j < allUsers.length; j++) {
				names[j] = allUsers[j].firstName + " " + allUsers[j].lastName;
			}
			
			setupAutocomplete(names);
		}
	});
	
	$(window).on("resize", function(){
		let classOnOff = window.matchMedia('(min-width: 450px)').matches;
		$(".form-check").toggleClass("form-check-inline", classOnOff);
	});
})

function loadMoreUsers() {
	
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
	
	usersToDisplay = users;
	
	index = 0;
	shown = 0;
	
	$("#userRow").empty();
	
	loadMoreUsers();
}

function setupAutocomplete(list) {
	var searchBar = $("#searchBar");
	
	searchBar.on("input", function(){
		
		var val = this.value;
		var matchingUsers = new Array();
					
		for(let i = 0; i < list.length; i++) {
			if(list[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
				matchingUsers.push(allUsers[i]);
			}
		}
		
		loadUsers(matchingUsers);
	});
}