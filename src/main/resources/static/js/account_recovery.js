$(function() {

	//non funziona per tutte, ma è leggibile e riconosce gli errori più comuni
	const emailRegex = '^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$';

	$(".empty-field-error").remove();

	$('#recoverAccountButton').click(function(event) {
		$(".empty-field-error").remove();

		if (!$("#emailInput").val().length) {
			event.preventDefault();
			$("#insertEmail").append('<div class="empty-field-error">Inserisci una mail.</div>');
		} else if (!$("#emailInput").val().match(emailRegex)) {
			event.preventDefault();
			$("#insertEmail").append('<div class="empty-field-error">Inserisci una mail valida.</div>');
		}
	});

	$('#changePasswordButton').click(function(event) {
		$(".empty-field-error").remove();
		if (!$("#passwordInput").val().length) {
			event.preventDefault();
			$("#password").append('<div class="empty-field-error">Inserisci una password.</div>');
		} else if ($("#passwordInput").val() !== $("#passwordRepeatInput").val()) {
			event.preventDefault();
			$("#passwordRepeat").append('<div class="empty-field-error">Le password devono essere uguali.</div>');
		}
		if (!$("#emailInput").val().length) {
			event.preventDefault();
			$("#email").append('<div class="empty-field-error">Inserisci una email.</div>')
		}
		if(!$("#tokenInput").val().length) {
			event.preventDefault();
			$("#token").append('<div class="empty-field-error">Inserisci un token.</div>')
		}
	})

	$("#see-password").on("click", function() {
		if($(this).hasClass("fa-eye-slash")) {
			$(this).removeClass("fa-eye-slash");
			$(this).addClass("fa-eye");
			$("#passwordInput").attr("type", "text");
		}
		else {
			$(this).removeClass("fa-eye");
			$(this).addClass("fa-eye-slash");
			$("#passwordInput").attr("type", "password");
		}
	})
	
	$("#see-password-repeat").on("click", function() {
		if($(this).hasClass("fa-eye-slash")) {
			$(this).removeClass("fa-eye-slash");
			$(this).addClass("fa-eye");
			$("#passwordRepeatInput").attr("type", "text");
		}
		else {
			$(this).removeClass("fa-eye");
			$(this).addClass("fa-eye-slash");
			$("#passwordRepeatInput").attr("type", "password");
		}
	})

})

function fireSuccessAlert() {
	Swal.fire({
		title: 'Password Reimpostata!',
		text: 'La password è stata reimpostata con successo.',
		icon: 'success'
	}).then(function() {
		document.location.href = '/';
	});
}

function fireTokenExpiredAlert() {
	Swal.fire({
		title: 'Token scaduto',
		text: 'Il token è scaduto.',
		icon: 'error'
	}).then(function() {
		document.location.href = '/';
	})
}

function fireWrongTokenAlert() {
	Swal.fire({
		title: 'Token errato',
		text: "Il token è errato, controlla che sia stato digitato correttamente.",
		icon: 'error'
	})
}