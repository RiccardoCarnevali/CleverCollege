$(document).ready(function () {

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

    $("#professor").on('input', function () {

    })

});

