var like = "";
var type = "locations";
var locations = new Array();
var errorShown = false;

$(function() {
	
	loadMore(false);
	
	$("#searchBar").on("input", function() {
		like = $(this).val();
		loadMore(false);
	})
	
	$("#typeInput").on("change", function() {
		type = $(this).val();
		locations = new Array();
		$("#locations").empty();
		loadMore(false);
	})
})

function loadMore(showMore) {
	
	if(showMore)
		offset = locations.length;
	else
		offset = 0;
	
	$.ajax({
		type: "POST",
		url: "loadLocations",
		data: {
			type: type,
			like: like,
			offset: offset
		},
		success: function(data) {
			
			var showMoreButton = $("#showMoreButton");
			if(showMoreButton != null) {
				showMoreButton.remove();
			}
			
			if(locations.length != 0 && data.length != 0){
				if(areEquals(data.slice(0,15), locations)) {
					if(data.length == 16) {
						$("#locationsContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
						$("#showMoreButton").on("click", function() {
							loadMore(true);
						});
					}
					return;
				}
			}
			
			if(!showMore) {
				locations = new Array();
				$("#locations").empty();
			}
						
			locations = locations.concat(data.slice(0,15));
			if(locations.length <= 15)
				index = 0;
			
			var locationsList = $("#locations");
			for(; index < locations.length; index++) {
				locationsList.append("	<li class='list-group-item location'>" +
											"<span class='location-name'>" + locations[index].name + "</span>" +
											"<div class='icons' style='float:right'>" +
												"<span class='clickable modify-button' style='display:block;margin-bottom:20px'><i class='fas fa-pen'></i></span>" +
												"<span class='clickable remove-button' id='remove-" + locations[index].id + "'><i class='fas fa-trash'></i></span>" +
											"</div>" +
											"<span class='location-capacity' style='display:block'>Capacità massima: " + locations[index].capacity + " persone</span>" +
										"</li>");
			}
			
			$(".remove-button").on("click", function() {
				let id = this.id.substr(7);
				Swal.fire({
					title: "Sei sicuro?",
					text: "Sei sicuro di voler rimuovere questo luogo?\Tutti i dati inerenti ad esso verranno rimossi",
					icon: "warning",
					confirmButtonText: "Continua",
					showCancelButton: true,
					cancelButtonText: "Cancella"
				}).then((result) => {
					if(result.isConfirmed) {
						
						$.ajax({
							type: "POST",
							url: "checkClassroomsActivities",
							data: {
								classroom: id
							},
							success: function(data) {
								if(data == "yes") {
									Swal.fire({
										title: "Attenzione",
										text: "Questo luogo è un'aula in cui sono attualmente in programma delle attività, rimuovendola verranno rimosse anche tali attività.\nÈ consigliato avvertire i professori che le hanno pianificate.",
										icon: "warning",
										confirmButtonText: "Procedi comunque",
										showCancelButton: true,
										cancelButtonText: "Cancella"
									}).then((result) => {
										if (result.isConfirmed) {
											removeLocation(id);
										}
									})
								}
								else {
									removeLocation(id);
								}
							}
						})
					}
				});
			});
			
			if(data.length == 16) {
				$("#locationsContainer").append("<button class=\"btn btn-outline-primary\" id=\"showMoreButton\">Mostra altri</button>");
				$("#showMoreButton").on("click", function() {
					loadMore(true);
				});
			}
			
		},
		error: errorMessage 
	});
}

function removeLocation(id) {
	
	$.ajax({
		type: "POST",
		url: "removeLocation",
		data: {
			id: id
		},
		success: function(data) {
			if(data == "ok") {
				Swal.fire({
					title: "Successo!",
					text: "Il luogo è stato eliminato con successo",
					icon: "success"
				})
				locations = new Array();
				$("#locations").empty();
				loadMore();
			}
		},
		error: errorMessage
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

function areEquals(locations1, locations2) {
	if(locations1.length != locations2.length)
		return false;
		
	for(let i = 0; i < locations1.length; i++) {
		if(locations1[i].id != locations2[i].id)
			return false;
	}
	
	return true;
}