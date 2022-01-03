
$(function() {
	var error = false;

	if($("#fiscalCodeInput").hasClass('error-color')) {
		error = true;
		$("#fiscalCode").append('<div class="login-error">Codice fiscale o password errati.</div>')
	}
	
	//controlla che i campi non siano vuoti
	$("#login").click(function(event) {
		$(".empty-field-error").remove();
		
		if(!$("#fiscalCodeInput").val().length) {
			event.preventDefault();
			if(!error)
				$("#fiscalCode").append('<div class="empty-field-error">Inserisci il codice fiscale.</div>')
		}
		
		if(!$("#passwordInput").val().length) {
			event.preventDefault();
			if(!error)
				$("#password").append('<div class="empty-field-error">Inserisci la password.</div>')
		}	
	})
	
	//rimuovi le scritte di errore se i campi vanno out of focus
	$("#fiscalCodeInput").blur(function() {
		$("#fiscalCode .empty-field-error").remove();
		if(!$("#fiscalCodeInput").val().length && !error)
			$("#fiscalCode").append('<div class="empty-field-error">Inserisci il codice fiscale</div>')
	})
	
	$("#passwordInput").blur(function() {
		$("#password .empty-field-error").remove();
		if(!$("#passwordInput").val().length && !error)
			$("#password").append('<div class="empty-field-error">Inserisci la password</div>')
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
})


