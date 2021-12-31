var like = "";
var courses = new Array();
var errorShown = false;

$(function() {
	
	loadMore(false);
	
	$("#searchBar").on("input", function() {
		like = $(this).val();
		loadMore(false);
	})
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
			offset: offset
		},
		success: function(data) {
			
			var showMoreButton = $("#showMoreButton");
			if(showMoreButton != null) {
				showMoreButton.remove();
			}
			
			if(courses.length != 0 && data.length != 0){
				if(areEquals(data, courses))
					return;
			}
			
			if(!showMore) {
				courses = new Array();
				$("#courses").empty();
			}
						
			courses = courses.concat(data.slice(0,15));
			if(courses.length <= 15)
				index = 0;
			
			var coursesList = $("#courses");
			for(; index < courses.length; index++) {
				coursesList.append(	"	<li class='list-group-item course'>" +
											"<span class='course-name'>" + courses[index].name + "</span>" +
											"<div class='icons'>" +
												"<a href='#0' class='modify-button'><i class='fas fa-pen' style='float:right'></i></a>" +
													"<br /><br />" +
												"<a href='#0' class='remove-button' id='remove-" + courses[index].id + "'><i class='fas fa-trash' style='float:right'></i></a>" +
											"</div>" +
											"<br />" +
											"<span class='professor-name'>" + courses[index].lecturer.firstName + " " + courses[index].lecturer.lastName + "</span>" +
										"</li>");
			}
			
			$(".remove-button").on("click", function() {
				let id = this.id.substr(7);
				Swal.fire({
					title: "Sei sicuro?",
					text: "Sei sicuro di voler rimuovere questo corso?\Tutti i dati inerenti ad esso verranno rimossi",
					icon: "warning",
					confirmButtonText: "Continua",
					showCancelButton: true,
					cancelButtonText: "Cancella"
				}).then((result) => {
					if(result.isConfirmed) {
						removeCourse(id);
					}
				});
			});
			
			if(data.length == 16) {
				$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
				$("#showMoreButton").on("click", function() {
					loadMore(true);
				});
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
		error: errorMessage()
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
		if(courses1[i].cf != courses2[i].cf)
			return false;
	}
	
	return true;
}