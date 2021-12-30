$(document).ready(function () {
    var radioButton = document.getElementsByName("kindOfUser");
    for(let i = 0, lenght = radioButton.length; i < lenght; i++) {
        radioButton[i].addEventListener("click", function () {
            if(document.getElementById("student").checked) {
                $("#idStudent").css('display', 'block');
                $("#label-for-Student").css('display', 'block');
                $("#insert-user-button").prop('disabled', true);

            }
            else {
                $("#idStudent").css('display', 'none');
                $("#label-for-Student").css('display', 'none');
            }
        });
    }

    var radioButton = document.getElementsByName("kindOfData");
    for(let i = 0, length = radioButton.length; i < length; i++) {
        radioButton[i].addEventListener("click", function () {
            if(document.getElementById("place").checked) {
                $("#div-for-place").css('display', 'block');
                $("#div-for-professor").css('display', 'none');
            }
            else {
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
            if($("#fiscalCodeUser").val().length > 0 && $("#name").val().length > 0 &&
                $("#surname").val().length > 0 && $("#email").val().length > 0 &&
                !document.getElementById("idStudent").checked) {
                    $("#insert-user-button").prop('disabled', false);
            }
            else if($("#fiscalCodeUser").val().length > 0 && $("#name").val().length > 0 &&
                $("#surname").val().length > 0 && $("#email").val().length > 0 && $("#idStudent").val().length > 0 &&
                document.getElementById("idStudent").checked){
                $("#insert-user-button").prop('disabled', false);
            }
            else
                $("#insert-user-button").prop('disabled', true);
        });
    }

});

