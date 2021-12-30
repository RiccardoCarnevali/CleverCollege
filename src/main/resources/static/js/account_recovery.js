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
	})


})

function fireSuccessAlert() {
	Swal.fire({
		title: 'Password Reimpostata!',
		text: 'La password è stata reimpostata con successo.',
		type: 'success'
	}).then(function() {
		document.location.href = '/';
	});
}

function fireTokenExpiredAlert() {
	Swal.fire({
		title: 'Token scaduto',
		text: 'Il token è scaduto.',
		type: 'failure'
	}).then(function() {
		document.location.href = '/';
	})
}

function fireWrongTokenAlert() {
	Swal.fire({
		title: 'Token errato',
		text: "Il token è errato, controlla che sia stato digitato correttamente.",
		type: 'failure'
	})
}