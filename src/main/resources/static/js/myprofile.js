var dLayout = null; 
var dLayoutShow = null;
var description = "";

$(function() {
	$("#modDescription").on("click", editMode);
	dLayout = $("#descriptionLayout");
	description = $("#description").text();
	dLayoutShow = dLayout.contents();
	
	$("#modPP-icon").on("click", function() {
		$("#modPP").trigger("click");
	})
	
	$("#modPP").on("input", updateImage);
	
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
        }
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
	});
}

var editMode = function() {
	$("#modDescription").hide();
	dLayout.empty();
	dLayout.append("<textarea id='description-text-area' maxlength='256' class='form-control'>" + description + "</textarea>");
	dLayout.append("<button type='button' id='confirmMod' class='btn btn-outline-primary btn-sm'>Conferma Modifica</button>"); 
	dLayout.append("<button type='button' id='abortMod' class='btn btn-outline-secondary btn-sm'>Annulla Modifica</button>");
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
				}
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
				success: console.log("img done")
			})
			
			reader = new FileReader()
			reader.onload = function() {
				$("#profile-picture").replaceWith("<img id='profile-picture' class='card-img-top rounded-circle' alt='ma guarda quanto sei bello/a' src='"+ reader.result + "'>");
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
