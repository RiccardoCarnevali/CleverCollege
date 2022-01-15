var dLayout = null; 
var dLayoutShow = null;
var description = "";

$(function() {
	$("#modPP-icon").on("click", function() {
		$("#modPP").trigger("click");
	})
	$("#modPP").on("input", updateImage);

	$("#modDescription").on("click", editMode);
	dLayout = $("#descriptionLayout");
	description = $("#description").text();
	dLayoutShow = dLayout.contents();

	$("#changePassword").on("click", changePasswordEdit);

})

var updateDescription = function() {	
	description = $("#description-text-area").val();
	console.log(description);
	$.ajax({
		type: 'POST',
		url: 'updateDescription',
		data: {
			description: description	
		},
		success: showMode,
		error: errorMessage
	});
}

var editMode = function() {
	$("#modDescription").hide();
	dLayout.empty();
	dLayout.append("<textarea id='description-text-area' maxlength='256' class='form-control'>" + description + "</textarea>");
	dLayout.append("<button type='button' id='confirmMod' class='btn btn-outline-primary btn-sm'>Conferma Modifica</button>"); 
	dLayout.append("<button type='button' id='abortMod' class='btn btn-outline-danger btn-sm'>Annulla Modifica</button>");
	$("#abortMod, #confirmMod").css("margin-top", "10px");
	$("#abortMod").css("float", "right");
	$("#confirmMod").on("click", updateDescription);
	$("#abortMod").on("click", showMode);
}

var showMode = function() {
	$("#modDescription").show();
	dLayout.empty();
	dLayout.append("<label>"+ description + "</label>");
}

var updateImage = function() {
	var file = $("#modPP").get(0).files[0];
	
	formData = new FormData();
	formData.append("image", file);
	$.ajax({
		type:'POST',
		url: 'updateProfilePicture',
		data: formData,
		contentType: false,
		processData: false,
		success: function(data) {
			if (data == 'error') {
				Swal.fire({
					icon: 'error',
					title: 'Qualcosa è andato storto',
					text: "Non è stato possibile aggiornare l'immagine del profilo. Riprova più tardi"
				});
			}
			else if(data == "img too big") {
				 Swal.fire({
					icon: 'error',
					title: 'Immagine non valida',
					text: "Impossibile aggiornare il profilo. L'immagine selezionata è più grande di 1MB."
				});
			}
			else if(data == "not an img") {
				Swal.fire({
					icon: 'error',
					title: 'File non valido',
					text: "Impossibile aggiornare il profilo. Il file selezionato non è un'immagine."
				});
			}
			else if(data == "img too small") {
				Swal.fire({
					icon: 'error',
					title: 'Immagine non valida',
					text: "Impossibile aggiornare il profilo. L'immagine selezionata è troppo piccola."
				});
			}
			else if(data == "ok") {
				reader = new FileReader()
				reader.onload = function() {
					$("#profile-picture").attr("src", reader.result);
				}	
				reader.readAsDataURL(file);
			}
		},
		error: errorMessage
	});
}

var changePassword = function() {
	if($("#current-password").val()) {
		if($("#new-password").val() && $("#new-password").val() == $("#new-password-confirm").val()) {
			$.ajax({
				type: 'POST',
				url: 'updatePassword',
				data: {
					oldPwd: $("#current-password").val(),
					newPwd: $("#new-password").val(),
					confirmPwd: $("#new-password-confirm").val()
				},
				success: function(result) {
					switch(result) {
						case "correct":
							swal_password_out.correct();
							changePasswordRestore();
							break;
						case "wrong password":
							swal_password_out.passwordError();
							break;
						case "new pwd not matching":
							swal_password_out.confirmError();
							break;
						case "pwd missing":
							swal_password_out.currentMissing();
							break;
						case "error":
							window.alert("error");
							break;
					}
				},
				error: errorMessage
			});
		}
		else {
			swal_password_out.confirmError();
		}
	}
	else {
		swal_password_out.currentMissing();
	}
}

var changePasswordEdit = function() {
	$("#passwordChangeLayout").empty();
	$("#passwordChangeLayout").append("<label for='current-password' style='display: block'>Inserisci la tua password corrente:</label>" +
									 "<div style='position:relative'>" +
										 "<input type='password' id='current-password' maxlength='30' class='form-control' style='display: block' placeholder='Password corrente'></input>" +
										 "<i class='fas fa-eye-slash clickable' id='see-current-password'></i>" +
									 "</div>" +
									 "<label for='new-password' style='display: block'>Inserisci la nuova password:</label>" +
									 "<div style='position:relative'>" +
										 "<input type='password' id='new-password' maxlength='30' class='form-control' style='display: block' placeholder='Nuova password'></input>" +
										 "<i class='fas fa-eye-slash clickable' id='see-new-password'></i>" +
									 "</div>" +
									 "<label for='new-password' style='display: block'>Conferma la nuova password:</label>" +
									 "<div style='position:relative'>" +
										 "<input type='password' id='new-password-confirm' maxlength='30' class='form-control' style='display: block' placeholder='Conferma password'></input>" +
										 "<i class='fas fa-eye-slash clickable' id='see-new-password-confirm'></i>" +
									 "</div>" +
									 "<button type='button' id='password-confirmMod' class='btn btn-outline-primary btn-sm'>Conferma Modifica</button>" +
									 "<button type='button' id='password-abortMod' class='btn btn-outline-danger btn-sm'>Annulla Modifica</button>");
	$("#password-confirmMod").on("click", changePassword);
	$("#password-abortMod").on("click", changePasswordRestore);
	
	$("#see-current-password").on("click", function() {
		if($(this).hasClass("fa-eye-slash")) {
			$(this).removeClass("fa-eye-slash");
			$(this).addClass("fa-eye");
			$("#current-password").attr("type", "text");
		}
		else {
			$(this).removeClass("fa-eye");
			$(this).addClass("fa-eye-slash");
			$("#current-password").attr("type", "password");
		}
	})
	
	$("#see-new-password").on("click", function() {
		if($(this).hasClass("fa-eye-slash")) {
			$(this).removeClass("fa-eye-slash");
			$(this).addClass("fa-eye");
			$("#new-password").attr("type", "text");
		}
		else {
			$(this).removeClass("fa-eye");
			$(this).addClass("fa-eye-slash");
			$("#new-password").attr("type", "password");
		}
	})
	
	$("#see-new-password-confirm").on("click", function() {
		if($(this).hasClass("fa-eye-slash")) {
			$(this).removeClass("fa-eye-slash");
			$(this).addClass("fa-eye");
			$("#new-password-confirm").attr("type", "text");
		}
		else {
			$(this).removeClass("fa-eye");
			$(this).addClass("fa-eye-slash");
			$("#new-password-confirm").attr("type", "password");
		}
	})
}

var changePasswordRestore = function() {
	$("#passwordChangeLayout").empty();
	$("#passwordChangeLayout").append("<button type='button' id='changePassword' class='btn btn-outline-danger card-link'>Cambia password</button>");
	$("#changePassword").on("click", changePasswordEdit);
}

var swal_password_out = {
	currentMissing: function() {
		Swal.fire({
			icon: 'error',
			title: 'Password corrente mancante',
			text: 'Inserisci la tua password corrente e riprova'
		});
	},
	confirmError: function() { 
		Swal.fire({
			icon: 'error',
			title: 'Le due password non coincidono',
			text: 'Controlla di aver inserito correttamente la nuova password e riprova'
		});
	},
	passwordError: function() {
		Swal.fire({
			icon: 'error',
			title: 'Password errata',
			text: 'Controlla di aver inserito correttamente la vecchia password e riprova'
		});		
	}, 
	correct: function() {
		Swal.fire({
			icon: 'success',
			title: 'Cambio password riuscito',
			text: 'La password è stata cambiata correttamente'
		});
	}
}