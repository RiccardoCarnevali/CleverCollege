$(document).ready(function () {
	
	if (document.getElementById("student").checked) {
                $("#idStudent").css('display', 'block');
                $("#label-for-Student").css('display', 'block');
                checkField();

    } else {	
        $("#idStudent").css('display', 'none');
        $("#label-for-Student").css('display', 'none');
        checkField();
    }
	
    var radioButton = document.getElementsByName("kindOfUser");
    for (let i = 0, length = radioButton.length; i < length; i++) {
        radioButton[i].addEventListener("click", function () {
            if (document.getElementById("student").checked) {
                $("#idStudent").css('display', 'block');
                $("#label-for-Student").css('display', 'block');
                checkField();

            } else {
                $("#idStudent").css('display', 'none');
                $("#label-for-Student").css('display', 'none');
                checkField();
            }
        });
    }
    var forUserElement = document.getElementsByClassName("insert-user-element");
    for(let i = 0, length = forUserElement.length; i < length; i++) {
        $(forUserElement[i]).on('input', function () {
            checkField();
        });
    }

    $('#insert-user-button').on('click', function (e) {
        if(!stringContainsNumber($("#name").val()) && !stringContainsNumber($("#surname").val())) {
            e.preventDefault();
            var userFromForm;
            if(document.getElementById("student").checked) {
                if(stringContainsOnlyNumber($('#idStudent').val())) {
                    $('.loader').css('display', 'inline-block');
                    userFromForm = new Student($('#fiscalCodeUser').val(), $('#name').val(),
                        $('#surname').val(), $('#email').val(), null, null, null, $('#idStudent').val());
                    insertUser(JSON.stringify(userFromForm), $('input[name="kindOfUser"]:checked').val());
                }
                else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Errore!',
                        text: 'La matricola pu?? contenere solo numeri!'
                    });
                }
            }
            else {
                $('.loader').css('display', 'inline-block');
                userFromForm = new User($('#fiscalCodeUser').val(), $('#name').val(),
                    $('#surname').val(), $('#email').val(), null, null, null);
                insertUser(JSON.stringify(userFromForm), $('input[name="kindOfUser"]:checked').val());
            }
        }
        else {
            Swal.fire({
                icon: 'error',
                title: 'Errore!',
                text: 'Il nome e il cognome possono contenere solo lettere, non numeri.'
            });
        }
    });

    $("#cancel-insertion").on("click", function () {
        window.location = "/users";
    })

});

var stringContainsNumber = function(string){
    for(let i = 0; i < string.length; i++){
        if(string.charAt(i) === "1" || string.charAt(i) === "2" || string.charAt(i) === "3" || string.charAt(i) === "4"
            || string.charAt(i) === "5" || string.charAt(i) === "6" || string.charAt(i) === "7" ||
            string.charAt(i) === "8" || string.charAt(i) === "9" || string.charAt(i) === "0") {
            return true;
        }
    }
    return false;
}

var stringContainsOnlyNumber = function(string){
    for(let i = 0; i < string.length; i++){
        if(isNaN(string.charAt(i)))
            return false;
    }
    return true;
}

var checkField = function () {
    if($("#fiscalCodeUser").val().length > 0 && $("#name").val().length > 0 &&
        $("#surname").val().length > 0 && $("#email").val().length > 0 &&
        !document.getElementById("student").checked) {
            $("#insert-user-button").prop('disabled', false);
    }
    else if($("#fiscalCodeUser").val().length === 16 && $("#name").val().length > 0 &&
        $("#surname").val().length > 0 && $("#email").val().length > 0 && $("#idStudent").val().length < 10 &&
        $("#idStudent").val().length > 0 && document.getElementById("student").checked){
        //if(!stringContainsNumber($("#name").val()) && !stringContainsNumber($("#surname").val()) && !stringContainsOnlyNumber($("#idStudent").val()))
            $("#insert-user-button").prop('disabled', false);
    }
    else
        $("#insert-user-button").prop('disabled', true);
}

var insertUser = function (userFromForm, kindOfUser) {
    $.ajax({
        url: "/insertUser",
        method: "POST",
        data:  {
            userFromForm : userFromForm,
            kindOfUser : kindOfUser,
			update: $("#update").val()
        },
        success: function (response) {
            $('.loader').css('display', 'none');
            if(response === "no logged user") {
                window.location = '/login';
            }
            else if (response === "user already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'Il codice fiscale inserito corrisponde a un utente gi?? registrato!'
                });
            }
            else if (response === "email already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'L\'email inserita corrisponde a un utente gi?? registrato!'
                });
            }
            else if (response === "idStudent already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'La matricola inserita corrisponde a uno studente gi?? registrato!'
                });
            }
            else if (response === "cf not valid") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'Il codice fiscale inserito non ?? corretto!'
                });
            }
            else if (response === "server error") {
                errorMessage();
            }
            else {
                Swal.fire(
                    'Ben fatto!',
                    'L\'operazione ?? stata portata a termine con succcesso!',
                    'success'
                )
				
				if($("#update").val() === "false") {
			
	                $("#fiscalCodeUser").val('');
	                $("#name").val('');
	                $("#surname").val('');
	                $("#email").val('');
	                $("#idStudent").val('');
	                $("#insert-user-button").prop('disabled', true);
				}
            }
        },
        error: function () {
            $('.loader').css('display', 'none');
            errorMessage();
        }
    });
}
