var users = new Array();
var type = "users";
var sortBy = "cf";
var like = "";

$(function() {
	
	$("#searchBar").on("input", function() {
		like = $(this).val();
	})
	
	$("#searchButton").on("click", function() {
		users = new Array();
		$("#userRow").empty();
		loadMore();
	})
	
	$(document).keypress(function(e){
	    if (e.which == 13){
	        users = new Array();
			$("#userRow").empty();
			loadMore();
	    }
	});
	
	$("#typeInput").on("change", function() {
		type = $(this).val();
		users = new Array();
		$("#userRow").empty();
		loadMore();
	})
	
	$("#sortInput").on("change", function() {
		sortBy=$(this).val();
		users = new Array();
		$("#userRow").empty();
		loadMore();
	})
	
	$(window).on("resize", function(){
		let classOnOff = window.matchMedia('(min-width: 450px)').matches;
		$(".form-check").toggleClass("form-check-inline", classOnOff);
	});
	
	loadMore();
})

function loadMore() {
	
	$.ajax({
		type: "GET",
		url: "loadUsers",
		data: {
			type: type,
			sortBy: sortBy,
			like: like,
			offset: users.length
		},
		success: function(data) {
			
			var showMoreButton = $("#showMoreButton");
			if(showMoreButton != null) {
				showMoreButton.remove();
			}
			
			users = users.concat(data.slice(0, 6));
			if(users.length <= 6)
				index = 0;
			for(; index < users.length; index++) {
				$("#userRow").append(	"<div class=\"col-lg-2 col-md-4 col-sm-12 d-flex align-items-stretch\" id=\"card-" + users[index].cf + "\">" +
											"<div class=\"card\">" +
												//"<img class=\"card-img-top\" src=\"" + users[index].profilePicture + "\" alt=\"Card image\">" +
												"<img class=\"card-img-top\" src=\"assets/images/img_avatar1.png\" alt=\"Card image\">" +
													"<div class=\"card-body d-flex flex-column\">" +
														"<h4 class=\"card-title\">" + users[index].firstName + " " + users[index].lastName + "</h4>" +
												   		"<p class=\"card-text\">" + users[index].cf + "</p>" +
														"<div id=\"icons\">" +
															"<a href=\"#\" class=\"mt-auto align-self-start modify-button\"><i class=\"fas fa-pen\"></i></a>" +
													    	"<a href=\"#\" class=\"mt-auto align-self-end remove-button\" style=\"float:right\" id=\"" + users[index].cf + "\"><i class=\"fas fa-trash\"></i></a>" +
														"</div>" +
												 	"</div>" +
											"</div>" +
										"</div>");
				}
			
			$(".remove-button").on("click", function() {
				let cf = this.id;
				Swal.fire({
					title: "Sei sicuro?",
					text: "Sei sicuro di voler rimuovere questo utente?\nNon potrà più accedere alle funzionalità del sito e tutti i suoi dati verranno rimossi",
					icon: "warning",
					confirmButtonText: "Continua",
					showCancelButton: true,
					cancelButtonText: "Cancella"
				}).then((result) => {
					if(result.isConfirmed) {
						$.ajax({
							type: "POST",
							data : {
								cf : cf
							},
							url: "removeUser",
							success : function(data) {
								if(data == "ok") {
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
							error : errorMessage
						})
					}
				});
			})
			
			if(data.length == 7)
				$("#dataContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
			$("#showMoreButton").on("click", function() {
				loadMore();
			});
		},
		error : errorMessage
	})
}

function errorMessage() {
	Swal.fire({
		title: "Oops...",
		text: "Qualcosa è andato storto.",
		icon: "error"
	});
}