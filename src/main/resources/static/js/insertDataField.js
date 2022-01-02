$(document).ready(function () {
    var radioButton = document.getElementsByName("kindOfUser");
    for(let i = 0, lenght = radioButton.length; i < lenght; i++) {
        radioButton[i].addEventListener("click", function () {
            if(document.getElementById("student").checked) {
                $("#idStudent").css('display', 'block');
                $("#label-for-Student").css('display', 'block');
                checkField();

            }
            else {
                $("#idStudent").css('display', 'none');
                $("#label-for-Student").css('display', 'none');
                checkField();
            }
        });
    }

    var radioButton = document.getElementsByName("kindOfData");
    for(let i = 0, length = radioButton.length; i < length; i++) {
        radioButton[i].addEventListener("click", function () {
            if(document.getElementById("place").checked) {
                $("#kindOfPlace").css('display', 'block');
                $("#div-for-place").css('display', 'block');
                $("#div-for-professor").css('display', 'none');
            }
            else {
                $("#kindOfPlace").css('display', 'none');
                $("#div-for-professor").css('display', 'block');
                $("#div-for-place").css('display', 'none');
            }
        });
    }

    $("#dataName").on('input', function () {
        if($("#dataName").val().length === 0) {
            $("#insert-other-data-button").prop('disabled', true);
        }
        else {
            $("#insert-other-data-button").prop('disabled', false);
        }
    });

    var forUserElement = document.getElementsByClassName("insert-user-element");
    for(let i = 0, length = forUserElement.length; i < length; i++) {
        $(forUserElement[i]).on('input', function () {
            checkField();
        });
    }


    $('#insert-user-button').on('click', function (e) {
        $('.loader').css('display', 'inline-block');
        e.preventDefault();
        insertUser($('#fiscalCodeUser').val(), $('#name').val(),
            $('#surname').val(), $('#email').val(), $('#idStudent').val(), $('input[name="kindOfUser"]:checked').val());
    });


});

var checkField = function () {
    if($("#fiscalCodeUser").val().length > 0 && $("#name").val().length > 0 &&
        $("#surname").val().length > 0 && $("#email").val().length > 0 &&
        !document.getElementById("student").checked) {
        $("#insert-user-button").prop('disabled', false);
    }
    else if($("#fiscalCodeUser").val().length === 16 && $("#name").val().length > 0 &&
        $("#surname").val().length > 0 && $("#email").val().length > 0 && $("#idStudent").val().length < 10 &&
        $("#idStudent").val().length > 0 && document.getElementById("student").checked){
        $("#insert-user-button").prop('disabled', false);
    }
    else
        $("#insert-user-button").prop('disabled', true);
}

var insertUser = function (cf, name, surname, email, idStudent, kindOfUser) {
    $.ajax({
        url: "insertUser",
        method: "POST",
        data: {
            cf: cf,
            name: name,
            surname: surname,
            email: email,
            idStudent: idStudent,
            kindOfUser: kindOfUser
        },
        success: function (response) {
            $('.loader').css('display', 'none');
            if (response === "user already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'Il codice fiscale inserito corrisponde a un utente già registrato!'
                });
            }
            else if (response === "email already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'L\'email inserita corrisponde a un utente già registrato!'
                });
            }
            else if (response === "idStudent already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'La matricola inserita corrisponde a uno studente già registrato!'
                });
            }
            else if (response === "cf not valid") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'Il codice fiscale inserito non è corretto!'
                });
            }
            else {
                Swal.fire(
                    'Ben fatto!',
                    'Inserimento avvenuto con successo!',
                    'success'
                )
            }
        },
        fail: function (jqXHR, textStatus) {
            $('.loader').css('display', 'none');
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Qualcosa è andato storto!'
            });
        }
    });
}
