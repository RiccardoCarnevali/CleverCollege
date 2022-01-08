var selectedProfessor = null;
$(document).ready(function () {
	
	selectedProfessor = $("#lecturer").val();	
	
    if($("#place").is(":checked")) {
        $("#kindOfPlace").css('display', 'block');
        $("#div-for-place").css('display', 'block');
        $("#div-for-professor").css('display', 'none');
        checkField();
    }
    else {
        $("#kindOfPlace").css('display', 'none');
        $("#div-for-professor").css('display', 'block');
        $("#div-for-place").css('display', 'none');
        checkField();
    }

	$("input[name='kindOfData']").on("change", function() {
        if($("#place").is(":checked")) {
            $("#kindOfPlace").css('display', 'block');
            $("#div-for-place").css('display', 'block');
            $("#div-for-professor").css('display', 'none');
            checkField();
        }
        else {
            $("#kindOfPlace").css('display', 'none');
            $("#div-for-professor").css('display', 'block');
            $("#div-for-place").css('display', 'none');
            checkField();
        }
	})

    var inputText = document.getElementsByClassName("form-control");
    for(let i = 0, length = inputText.length; i < length; i++) {
        inputText[i].addEventListener("input" , function () {
            checkField();
        });
    }

    var inputField = document.getElementById("professor");
    inputField.oninput = function () {
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
            dataFromForm = new Course(null, $('#dataName').val(), null);
        insertData(JSON.stringify(dataFromForm), $('input[name="kindOfData"]:checked').val(), $('input[name="kindOfPlace"]:checked').val());
    });
});

var checkField = function () {
    if($("#dataName").val().length === 0 && document.getElementById("place").checked)
        $("#insert-other-data-button").prop('disabled', true);
    else if(!document.getElementById("place").checked && ($("#professor").val().length === 0 || $("#dataName").val().length === 0))
        $("#insert-other-data-button").prop('disabled', true);
    else {
        $("#insert-other-data-button").prop('disabled', false);
    }
}

var searchProfessorFromSubstring = function (substring) {
    $.ajax({
        url: "/loadUsers",
        method: "POST",
        data: {
			type: "professors",
			sortBy: "first_name",
			like: substring,
			offset: 0
        },
        success: function (responseData) {
            document.getElementsByClassName("professor-list")[0].innerHTML = "";
            if(responseData == null) {
                errorMessage();
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
        error: errorMessage
    });
}

var insertData = function (dataFromForm, kindOfData, kindOfPlace) {
    $.ajax({
        url: "/insertData",
        method: "POST",
        data:  {
            dataFromForm : dataFromForm,
            kindOfData : kindOfData,
            kindOfPlace : kindOfPlace,
			professorCf: selectedProfessor,
			id: $("#id").val()
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
                errorMessage();
            }
            else {
                Swal.fire(
                    'Ben fatto!',
                    'L\'operazione è stata portata a termine con successo!',
                    'success'
                )

				if($("#id").length == 0) {
	                $("#professor").val('');
	                $("#dataName").val('');
	                document.getElementsByClassName("professor-list")[0].innerHTML = "";
	                $("#insert-other-data-button").prop('disabled', true);
				}
            }
        },
        error: function () {
            $('.loader').css('display', 'none');
            errorMessage();
        }
    });
}
