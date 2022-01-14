var errorShown = false;

function errorMessage() {
	if (!errorShown) {
		errorShown = true;
		Swal.fire({
			title: "Oops...",
			text: "Qualcosa è andato storto.",
			icon: "error"
		}).then(() => {
			location.reload();
		})
	}
}