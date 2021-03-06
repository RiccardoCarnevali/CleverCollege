var like = "";
var courses = new Array();

$(function() {
	
	$("#searchBar").val("");
	
	$("#searchBar").on("input", function() {
		like = $(this).val();
		loadMore(false);
	})
	
	loadMore(false);
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
			type: "all",
			offset: offset
		},
		success: function(data) {
			var showMoreButton = $("#showMoreButton");
			if(showMoreButton != null) {
				showMoreButton.remove();
			}

			if(data.length === 0 && offset === 0) {
				var coursesList = $("#courses");
				coursesList.empty();
				courses = [];
				var message = "";
				if(like !== "") {
					message = "Nessun risultato soddisfa la ricerca.";
				}
				else
					message = "Nessun corso è stato ancora registrato.";
				coursesList.append("<li class='list-group-item' style='text-align: center; margin: 10px 0px'>" + message + "</li>");
				return;
			}
			
			if(courses.length != 0 && data.length != 0){
				if(areEquals(data.slice(0,15), courses)) {
					if(data.length == 16) {
						$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
						$("#showMoreButton").off().on("click", function() {
							loadMore(true);
						});
					}
					return;
				}
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
											"<div class='icons' style='float:right'>" +
												"<span class='clickable modify-button' id='modify-" + courses[index].id + "'style='display:block;margin-bottom:20px'><i class='fas fa-pen'></i></span>" +
												"<span class='clickable remove-button' id='remove-" + courses[index].id + "'><i class='fas fa-trash'></i></span>" +
											"</div>" +
											"<span class='professor-name' style='display:block'>" + courses[index].lecturer.firstName + " " + courses[index].lecturer.lastName + "</span>" +
										"</li>");
			}
			
			$(".remove-button").off().on("click", function() {
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
			
			$(".modify-button").off().on("click", function() {
				let id = this.id.substr(7);
				var form = $("	<form method='post' action='/courses/edit' style='display:none'>" +
									"<input type='text' name='courseId' value='" + id + "'>" +
								"</form>");
				$('body').append(form);
				form.submit();
			})
			
			if(data.length == 16) {
				$("#coursesContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
				$("#showMoreButton").off().on("click", function() {
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
		error: errorMessage
	})
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