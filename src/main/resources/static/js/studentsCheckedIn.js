var classroomChecked = null;
var listOfCheckedInStudent = null;
var listOfBookers = null;

$(document).ready(function () {

    var searchBar = document.getElementById("searchBar");
    searchBar.oninput = function () {
        if (searchBar.value !== "") {
            makeAllStudentsVisible();
            searchStudentsFromSubstring(searchBar.value);
        }
        else
            makeAllStudentsVisible();
    };

    $("#sortInput").on('change', function () {
        var chosenSort = $('#sortInput').find(":selected").text();
        if(chosenSort === "Nome") {
            document.getElementById("student-checked-in").innerHTML = "";
            listOfCheckedInStudent.sort((a, b) => {
                 return a.firstName.localeCompare(b.firstName);
            });
            insertStudentInList(listOfCheckedInStudent, false);
        }
        else if(chosenSort === "Cognome") {
            document.getElementById("student-checked-in").innerHTML = "";
            listOfCheckedInStudent.sort((a, b) => {
                return a.lastName.localeCompare(b.lastName);
            });
            insertStudentInList(listOfCheckedInStudent, false);
        }
        else {
            document.getElementById("student-checked-in").innerHTML = "";
            listOfCheckedInStudent.sort((a, b) => {
                return a.cf.localeCompare(b.cf);
            });
            insertStudentInList(listOfCheckedInStudent, false);
        }
    })

});

var searchStudentsFromSubstring = function (substring) {
    var allLiElements = document.getElementsByClassName("list-group-item");
    var allInformationElements = document.getElementsByClassName("student-name");
    for(let i = 0; i < allLiElements.length; i++) {
        var content = allInformationElements.item(i).innerHTML.toLowerCase();
        if(content.indexOf(substring.toLowerCase()) === -1) {
            allLiElements.item(i).style.display = "none";
        }
    }
}

var makeAllStudentsVisible = function () {
    var allLiElements = document.getElementsByClassName("list-group-item");
    for(let i = 0; i < allLiElements.length; i++) {
        allLiElements.item(i).style.display = "flex";
    }
}

var errorRedirect = function () {
    Swal.fire({
        title: "Oops...",
        text: "Qualcosa è andato storto.",
        icon: "error",
        timer: 2000
    }).then(() => {
        window.location = "/check-in";
    });
}

window.onload = function(e) {findClassroomChecked()};

var findClassroomChecked = function () {
    $.ajax({
        url: "/findClassroomChecked",
        method: "POST",
        success: function (response) {
            classroomChecked = response;
            if(classroomChecked === null)
                errorRedirect();
            else
                checkedInstudents();
        },
        error: function () {
            errorRedirect();
        }
    });
}

var checkedInstudents = function () {
    $.ajax({
        url: "/findCheckedInStudents",
        method: "POST",
        contentType:'application/json',
        data: JSON.stringify(classroomChecked),
        success: function (response) {
            listOfCheckedInStudent = response;
            if (listOfCheckedInStudent == null)
                errorRedirect();
            else if (response.length === 0) {
                var studentList = $("#student-checked-in");
                studentList.append("<li class='list-group-item student' style='text-align: center'>Nessuno studente ha effettuato il check-in in aula.</li>");
            } else
                findActivityInClassroomAndCheckBookers();
        },
        error: function () {
            errorRedirect();
        }
    });
}

var findActivityInClassroomAndCheckBookers = function () {
    $.ajax({
        url: "/findActivityInClassroomAndCheckBookers",
        method: "POST",
        contentType:'application/json',
        data: JSON.stringify(classroomChecked),
        success: function (response) {
            listOfBookers = response;
            if(listOfBookers === null)
                errorRedirect();
            else {
                insertStudentInList(listOfCheckedInStudent, true);
            }
        },
        error: function () {
            errorRedirect();
        }
    });
}

var insertStudentInList = function(checkedInStudent, firstInsert) {
    for (let i = 0, max = checkedInStudent.length; i < max; i++) {
        var booked = null;
        //map mappa ogni elemento dell'array con un altro elemento in base al valore specificato nell'operazione della lambda
        if(listOfBookers.map((l) => l.cf).includes(checkedInStudent[i].cf))
            booked = "Prenotato all'attività.";
        else
            booked = "Non prenotato all'attività.";
        var studentList = $("#student-checked-in");
        studentList.append("<li id='"+ checkedInStudent[i].cf +"' class='list-group-item student' style='display:flex'>" +
            "<div style='width: 90%'><span class='student-name'>" + checkedInStudent[i].firstName + " " + checkedInStudent[i].lastName + "</span>" +
            "<span class='other-student-info' style='display:block'> Codice fiscale: " + checkedInStudent[i].cf + "</span>" +
            "<span class='other-student-info' style='display:block'>" + booked + "</span></div>" +
            "<div class='icons' style='margin:auto'>" +
            "<span class='clickable forced-checked-out' style='display:block;'>" +
            "<i class='fas fa-door-open'></i></span>" +
            "</div>" +
            "</li>");
        if(!firstInsert && (checkedInStudent[i].firstName + " " + checkedInStudent[i].lastName).toLowerCase().indexOf(document.getElementById("searchBar").value.toLowerCase()) === -1)
            document.getElementById(checkedInStudent[i].cf).style.display = "none";
    }
    var checkOutButton = document.getElementsByClassName("forced-checked-out");
    for(let i = 0, length = checkOutButton.length; i < length; i++) {
        checkOutButton[i].addEventListener('click', function () {
            forcedCheckOut(checkOutButton[i].parentElement.parentElement.id);
        });
    }
}

var forcedCheckOut = function (studentId) {
    $.ajax({
        url: "/forcedStudentCheckOut",
        method : "POST",
        data: {
            studentId: studentId
        },
        success: function (response) {
            if(response ===  "server error")
                errorMessage();
            else {
                document.getElementById(studentId).remove();
                if($("#student-checked-in").length === 1) {
                    $("#student-checked-in").append("<li class='list-group-item student' style='text-align: center'>Nessuno studente ha effettuato il check-in in aula.</li>");
                }
                Swal.fire(
                    'Rimozione avvenuta!',
                    'Utente rimosso dai presenti in aula!',
                    'success'
                );
            }
        },
        error: function () {
            errorMessage();
        }
    });
}

