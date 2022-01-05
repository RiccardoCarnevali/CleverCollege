var selectedProfessor = null;
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

    var inputField = document.getElementById("professor");
    inputField.onkeyup = function () {
        selectedProfessor = null;
        if(inputField.value !== "")
            searchProfessorFromSubstring(inputField.value);
        else
            document.getElementsByClassName("professor-list")[0].innerHTML = "";
    };

    $('#insert-other-data-button').on('click', function (e) {
        $('.loader').css('display', 'inline-block');
        e.preventDefault();
        var dataFromForm;
        if(document.getElementById("place").checked)
            dataFromForm = new Location(null, $('#dataName').val(), $('#capacity').val());
        else
            dataFromForm = new Location(null, $('#dataName').val(), null);
        alert(selectedProfessor);
        insertData(JSON.stringify(dataFromForm), $('input[name="kindOfData"]:checked').val(), $('input[name="kindOfPlace"]:checked').val(), selectedProfessor);
    });

});

function Course(id, name, lecturer) {
    this.id = id;
    this.name = name;
    this.lecturer = lecturer;
}

function Location(id, name, capacity) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
}

var searchProfessorFromSubstring = function (substring) {
    $.ajax({
        url: "searchProfessor",
        method: "POST",
        data: {
            substring : substring
        },
        success: function (responseData) {
            document.getElementsByClassName("professor-list")[0].innerHTML = "";
            if(responseData == null) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Qualcosa è andato storto!'
                });
            }
            else {
                for (let i in responseData){
                    var liNode = document.createElement("LI");
                    var textnode = document.createTextNode(responseData[i].firstName + " " + responseData[i].lastName);
                    liNode.appendChild(textnode);
                    liNode.addEventListener("click", function () {
                        selectedProfessor = responseData[i].cf;
                        document.getElementById("professor").value = responseData[i].firstName + " " + responseData[i].lastName;
                        document.getElementsByClassName("professor-list")[0].innerHTML = "";
                    })
                    document.getElementsByClassName("professor-list")[0].appendChild(liNode);
                }
            }
        },
        fail: function () {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Qualcosa è andato storto!'
            });
        }
    });
}

var insertData = function (dataFromForm, kindOfData, kindOfPlace, cfProfessor) {
    $.ajax({
        url: "insertData",
        method: "POST",
        data:  {
            dataFromForm : dataFromForm,
            kindOfData : kindOfData,
            kindOfPlace : kindOfPlace,
            cfProfessor : cfProfessor
        },
        success: function (response) {
            $('.loader').css('display', 'none');
            if(response === "no logged user") {
                window.location = '/login';
            }
            else if (response === "place already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'Il luogo inserito corrisponde a un luogo già registrato!'
                });
            }
            else if (response === "course already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'Il corso inserito corrisponde a un corso già registrato!'
                });
            }
            else if (response === "no prof selected") {
                Swal.fire({
                    icon: 'error',
                    title: 'Errore!',
                    text: 'Non hai selezionato alcun professore!'
                });
            }
            else if (response === "server error") {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Qualcosa è andato storto!'
                });
            }
            else {
                console.log(response);
                Swal.fire(
                    'Ben fatto!',
                    'Inserimento avvenuto con successo!',
                    'success'
                )
            }
        },
        fail: function () {
            $('.loader').css('display', 'none');
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Qualcosa è andato storto!'
            });
        }
    });
}
