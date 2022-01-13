var classroomChecked = null;
var listOfCheckedInStudent = null;
var listOfBookers = null;

$(document).ready(function () {

    var searchBar = document.getElementById("searchBar");
    searchBar.oninput = function () {
        var noElementLi = document.getElementsByClassName("no-element-class");
        for(let i = 0; i < noElementLi.length; i++)
            noElementLi.item(i).remove();
        if (searchBar.value !== "") {
            makeAllStudentsVisible();
            searchStudentsFromSubstring(searchBar.value);
        }
        else {
            makeAllStudentsVisible();
        }
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
    if(listOfCheckedInStudent.length === 0) {
        var studentList = $("#student-checked-in");
        studentList.empty();
        studentList.append("<li class='list-group-item student no-element-class' style='text-align:center'>Nessuno risultato soddisfa la ricerca.</li>");
        return;
    }
    var allLiElements = document.getElementsByClassName("list-group-item");
    var allInformationElements = document.getElementsByClassName("student-name");
    var visibleElement = 0;
    for(let i = 0; i < allLiElements.length; i++) {
        var content = allInformationElements.item(i).innerHTML.toLowerCase();
        if(content.indexOf(substring.toLowerCase()) === -1) {
            allLiElements.item(i).style.display = "none";
        }
        else
            visibleElement++;
    }
    if(visibleElement === 0) {
        var studentList = $("#student-checked-in");
        studentList.append("<li class='list-group-item student no-element-class' style='text-align:center'>Nessuno risultato soddisfa la ricerca.</li>");
    }
}

var makeAllStudentsVisible = function () {
    if(listOfCheckedInStudent.length === 0) {
        var studentList = $("#student-checked-in");
        studentList.empty();
        studentList.append("<li class='list-group-item student no-element-class' style='text-align:center'>Nessuno studente ha effettuato il check-in in aula.</li>");
        return;
    }
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
            else
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

    if(checkedInStudent.length === 0) {
        $("#student-checked-in").append("<li class='list-group-item student no-element-class' style='text-align: center'>Nessuno studente ha effettuato il check-in in aula.</li>");
    }

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
    for(let btn of checkOutButton) {
        btn.addEventListener('click', function () {
            forcedCheckOut(btn.parentElement.parentElement.id);
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
            if(response === "server error")
                errorMessage();
            else {
                for(let i = 0; i < listOfCheckedInStudent.length; i++) {
                    if(listOfCheckedInStudent[i].cf === studentId)
                        listOfCheckedInStudent.splice(i, 1);
                }
                document.getElementById(studentId).remove();
                if(listOfCheckedInStudent.length === 0) {
                    $("#student-checked-in").append("<li class='list-group-item student no-element-class' style='text-align: center'>Nessuno studente ha effettuato il check-in in aula.</li>");
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

