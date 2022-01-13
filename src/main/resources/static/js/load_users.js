var users = new Array();
var type = "users";
var sortBy = "cf";
var like = "";

$(function() {

	$("#typeInput").val("users"),
	$("#sortInput").val("cf"),
	$("#searchBar").val("");

	$("#searchBar").on("input", function() {
		like = $(this).val();
		loadMore(false);
	})

	$("#typeInput").on("change", function() {
		type = $(this).val();
		users = new Array();
		$("#rows").empty();
		loadMore(false);
	})

	$("#sortInput").on("change", function() {
		sortBy = $(this).val();
		users = new Array();
		$("#rows").empty();
		loadMore(false);
	})

	loadMore(false);
})

function loadMore(showMore) {

	if (showMore)
		offset = users.length;
	else
		offset = 0;

	$.ajax({
		type: "POST",
		url: "loadUsers",
		data: {
			type: type,
			sortBy: sortBy,
			like: like,
			offset: offset
		},
		success: function(data) {

			var showMoreButton = $("#showMoreButton");
			if (showMoreButton != null) {
				showMoreButton.remove();
			}

			if(data.length === 0 && offset === 0) {
				var usersList = $("#rows");
				usersList.empty();
				users = [];
				var message = "";
				if(like !== "") {
					message = "Nessun risultato soddisfa la ricerca.";
				}
				else
					message = "Nessun utente è stato ancora registrato.";
				usersList.append("<li class='list-group-item' style='text-align: center; margin: 10px 0px'>" + message + "</li>");
				return;
			}

			if (users.length != 0 && data.length != 0) {
				if (areEquals(data.slice(0,6), users)){
					if (data.length == 7) {
						$("#usersContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
						$("#showMoreButton").off().on("click", function() {
							loadMore(true);
						});
					}
					
					return;
				}
			}

			if (!showMore) {
				users = new Array();
				$("#rows").empty();
			}

			users = users.concat(data.slice(0, 6));
			if (users.length <= 6)
				index = 0;

			$("#rows").append("<div class=\"row user-row\"></div>");
			var userRow = $(".user-row").last();
			for (; index < users.length; index++) {
				
				var imgPath = users[index].profilePicture == null ? "assets/images/pp-placeholder.png" : "assets/images/pp/" + users[index].cf + ".png";
				
				userRow.append("<div class=\"col-lg-2 col-md-4 col-sm-12 d-flex align-items-stretch\">" +
									"<div class=\"card\">" +
										"<img class=\"card-img-top\" src='" + imgPath + "' alt=\"Card image\">" +
										"<div class=\"card-body d-flex flex-column\">" +
											"<h4 class=\"card-title\">" + users[index].firstName + " " + users[index].lastName + "</h4>" +
											"<p class=\"card-text\">" + users[index].cf + "</p>" +
											"<div class=\"icons\">" +
												"<span class=\"clickable mt-auto align-self-start modify-button\" id='modify-" + users[index].cf + "'><i class=\"fas fa-pen\"></i></span>" +
												"<span class=\"clickable mt-auto align-self-end remove-button\" style='float:right' id=\"remove-" + users[index].cf + "\"><i class=\"fas fa-trash\"></i></span>" +
											"</div>" +
										"</div>" +
									"</div>" +
								"</div>");
			}

			$(".remove-button").off().on("click", function() {
				let cf = this.id.substr(7, 16);
				Swal.fire({
					title: "Sei sicuro?",
					text: "Sei sicuro di voler rimuovere questo utente?\nNon potrà più accedere alle funzionalità del sito e tutti i suoi dati verranno rimossi",
					icon: "warning",
					confirmButtonText: "Continua",
					showCancelButton: true,
					cancelButtonText: "Cancella"
				}).then((result) => {
					if (result.isConfirmed) {
						$.ajax({
							type: "POST",
							url: "checkProfessorsCourses",
							data: {
								professor: cf
							},
							success: function(data) {
								if (data == "yes") {
									Swal.fire({
										title: "Attenzione",
										text: "Questo utente è un professore titolare di uno o più corsi, rimuovendolo verranno rimossi anche i corsi di cui è titolare.\nÈ consigliato cambiare il titolare di tali corsi prima di continuare.",
										icon: "warning",
										confirmButtonText: "Procedi comunque",
										showCancelButton: true,
										cancelButtonText: "Cancella"
									}).then((result) => {
										if (result.isConfirmed) {
											removeUser(cf);
										}
									})
								}
								else {
									removeUser(cf);
								}
							},
							error: errorMessage
						});
					}
				});
			});
			
			$(".modify-button").off().on("click", function () {
				let cf = this.id.substr(7, 16);
				var form = $("	<form method='post' action='/users/edit' style='display:none'>" +
									"<input type='text' name='userCf' value='" + cf + "'>" +
								"</form>");
				$('body').append(form);
				form.submit();
			})

			if (data.length == 7) {
				$("#usersContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
				$("#showMoreButton").off().on("click", function() {
					loadMore(true);
				});
			}
		},
		error: errorMessage
	})
}

function removeUser(cf) {
	$.ajax({
		type: "POST",
		url: "removeUser",
		data: {
			cf: cf
		},
		success: function(data) {
			if (data == "ok") {
				Swal.fire({
					title: "Successo!",
					text: "L'utente è stato eliminato con successo",
					icon: "success"
				})
				users = new Array();
				$("#userRow").empty();
				loadMore();
			}
			else {
				errorMessage();
			}
		},
		error: errorMessage
	});
}

function areEquals(users1, users2) {
	if (users1.length != users2.length)
		return false;

	for (let i = 0; i < users1.length; i++) {
		if (users1[i].cf != users2[i].cf)
			return false;	
	}

	return true;
}