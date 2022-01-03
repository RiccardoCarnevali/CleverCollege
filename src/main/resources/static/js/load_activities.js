var like = "";
var type = "all";
var courses = new Array();
var followed = new Array();
var errorShown = false;

$(function() {
	
	loadMore(false);
	
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

})

function loadMore(showMore) {
	
	if(showMore)
		offset = courses.length;
	else
		offset = 0;
	
	$.ajax({
		type: "POST",
		url: "loadCourses",
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
			
			if(courses.length != 0 && data.length != 0){
				if(areEquals(data.slice(0,15), courses)) {
					if(data.length == 16) {
						$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
						$("#showMoreButton").on("click", function() {
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
			
			if(type == "all") {
				$.ajax({
					type: "POST",
					url: "checkFollowed",
					contentType: "application/json",
					data: JSON.stringify(courses.slice(offset, offset + 15)),
					success: function(data2) {
						
						followed = followed.concat(data2);
						
						displayCourses();
						
						if(data.length == 16) {
							$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
							$("#showMoreButton").on("click", function() {
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
					$("#showMoreButton").on("click", function() {
						loadMore(true);
					});
				}
			}
		},
		error: errorMessage 
	});
}

function removeCourse(id) {
	
	$.ajax({
		type: "POST",
		url: "removeCourse",
		data: {
			id: id
		},
		success: function(data) {
			if(data == "ok") {
				Swal.fire({
					title: "Successo!",
					text: "Il corso è stato eliminato con successo",
					icon: "success"
				})
				courses = new Array();
				$("#courses").empty();
				loadMore();
			}
		},
		error: errorMessage
	})
}

function displayCourses() {
	
	var coursesList = $("#courses");
	
	for(; index < courses.length; index++) {
		
		var iconClass;
		
		if(followed[index]) {
			iconClass = "fas fa-heart";
		}
		else {
			iconClass = "far fa-heart";
		}
		
		coursesList.append(	"	<li class='list-group-item course'>" +
									"<span class='course-name'>" + courses[index].name + "</span>" +
									"<div class='icons' style='float:right'>" +
										"<span class='clickable add-favorite-button' id='add-favorite-" + courses[index].id + "' style='color:#7e57c2'><i class='" + iconClass + "'></i></span>" +
									"</div>" +
									"<span class='professor-name' style='display:block'>" + courses[index].lecturer.firstName + " " + courses[index].lecturer.lastName + "</span>" +
								"</li>");
	}
	
	$(".add-favorite-button").on("click", function() {
		
		var icon = this.childNodes[0];
		var id = this.id.substr(13);
		alert(id);
		if(icon.classList.contains("far")) {
			icon.classList.remove("far")
			icon.classList.add("fas");	
		}
		else {
			icon.classList.remove("fas")
			icon.classList.add("far");	
		}
	})
}

function errorMessage() {
	if(!errorShown) {
		errorShown = true;
		Swal.fire({
			title: "Oops...",
			text: "Qualcosa è andato storto.",
			icon: "error"
		}).then((result) =>  {
			if(result.isConfirmed) {
				location.reload();
			}
		})
	}
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