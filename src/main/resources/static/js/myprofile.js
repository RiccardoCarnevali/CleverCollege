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
	
	$.ajax({
		type: 'POST',
		url: 'putProfilePicture',
		xhr:function(){// Seems like the only way to get access to the xhr object
            var xhr = new XMLHttpRequest();
            xhr.responseType= 'blob'
            return xhr;
        },
        success: function(data){
			if (data.size != 0) {				
	            var url = window.URL || window.webkitURL;
	            $("#profile-picture").attr("src", url.createObjectURL(data));
			}
        },
		error: errorMessage
	});
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
	
	if (file.size > 1000000) Swal.fire({
		icon: 'error',
		title: 'Immagine non valida',
		text: "Impossibile aggiornare il profilo. L'immagine selezionata è più grande di 1MB."
	})
	else {
		formData = new FormData();
		formData.append("image", file);
		$.ajax({
				type:'POST',
				url: 'updateProfilePicture',
				data: formData,
				contentType: false,
				processData: false,
				success: function(data) {
					if (data == 'ok') 
					if (data == 'error') {
						Swal.fire({
							icon: 'error',
							title: 'Qualcosa è andato storto',
							text: "Non è stato possibile aggiornare l'immagine del profilo. Riprova più tardi"
						})
					}
				},
				error: errorMessage
			})
		var img = new Image();
		var url = window.URL || window.webkitURL;
		img.onload = function() {
			if (img.naturalWidth < 180 || img.naturalHeight < 180) {
				Swal.fire({
				icon: 'error',
				title: 'Immagine non valida',
				text: "Impossibile aggiornare il profilo. L'immagine selezionata è troppo piccola."
				})
				return;
			}
			formData = new FormData();
			formData.append("image", file);
			$.ajax({
				type:'POST',
				url: 'updateProfilePicture',
				data: formData,
				contentType: false,
				processData: false,
				error: errorMessage
			})
			
			reader = new FileReader()
			reader.onload = function() {
				$("#profile-picture").attr("src", reader.result);
			}	
			reader.readAsDataURL(file);	
		}
		img.onerror = function() {
			Swal.fire({
				icon: 'error',
				title: 'File non valido',
				text: "Impossibile aggiornare il profilo. Il file selezionato non è un'immagine."
			})
		}
		img.src = url.createObjectURL(file);
		
		
	}
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
									 "<input type='password' id='current-password' maxlength='50' class='form-control' style='display: block' placeholder='Password corrente'></input>" +
									 "<label for='new-password' style='display: block'>Inserisci la nuova password:</label>" +
									 "<input type='password' id='new-password' maxlength='30' class='form-control' style='display: block' placeholder='Nuova password'></input>" +
									 "<label for='new-password' style='display: block'>Conferma la nuova password:</label>" +
									 "<input type='password' id='new-password-confirm' maxlength='30' class='form-control' style='display: block' placeholder='Conferma password'></input>" +
									 "<button type='button' id='password-confirmMod' class='btn btn-outline-primary btn-sm'>Conferma Modifica</button>" +
									 "<button type='button' id='password-abortMod' class='btn btn-outline-danger btn-sm'>Annulla Modifica</button>");
	$("#password-confirmMod").on("click", changePassword);
	$("#password-abortMod").on("click", changePasswordRestore);
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