var classroomChecked = null;
var listOfStudentsCheckedIn = null;
var listOfBookers = null;

window.onload = findClassroomChecked();

var findClassroomChecked = function () {
    $.ajax({
        url: "/findClassroomChecked",
        method: "POST",
        success: function (response) {
            classroomChecked = JSON.parse(response);
            if(classroomChecked === null)
                errorMessage();
            findActivityInClassroomChecked(classroomChecked);
        },
        error: function () {
            errorMessage();
        }
    });
}

var studentsCheckedIn = function () {
    $.ajax({
        url: "/findStudentsCheckedIn",
        method: "POST",
        data: {
            classroomChecked : JSON.stringify(classroomChecked)
        },
        success: function (response) {
            listOfStudentsCheckedIn = JSON.parse(response);
            if(listOfStudentsCheckedIn == null)
                errorMessage();
            else if(response.length === 0) {
                var textnode = document.createTextNode("Nessuno studente ha effettuato il check-in all'interno dell'aula.");
                document.body.appendChild(textnode);
            }
            else
                findActivityInClassroomAndCheckBookers(classroomChecked);
        },
        error: function () {
            errorMessage();
        }
    });
}


var findActivityInClassroomAndCheckBookers = function () {
    $.ajax({
        url: "/findActivityInClassroomAndCheckBookers",
        method: "POST",
        data: {
            classroomChecked : JSON.stringify(classroomChecked)
        },
        success: function (response) {
            listOfBookers = JSON.parse(response);
            if(listOfBookers === null)
                errorMessage();
            else {
                for (let i, max = listOfStudentsCheckedIn.length; i < max; i++) {
                    var booked = null;
                    if(listOfBookers.includes(response[i]))
                        booked = "Prenotato all'attività.";
                    else
                        booked = "Non prenotato all'attività.";
                    var studentList = $("#student-checked-in");
                    studentList.append("<li class='list-group-item location'>" +
                        "<span class='location-name'>" + listOfStudentsCheckedIn[i].firstName + listOfStudentsCheckedIn[i].lastName + "</span>" +
                        "<div class='icons' style='float:right'>" +
                        "<span class='clickable modify-button' id='forced-checked-out-" + listOfStudentsCheckedIn[i].cf + "' style='display:block;margin-bottom:20px'>" +
                        "<i class='fas fa-door-open'></i></span>" +
                        "</div>" +
                        "<span class='location-capacity' style='display:block'>Codice fiscale: " + listOfStudentsCheckedIn[i].cf + "</span>" +
                        "<span class='location-capacity' style='display:block'>booked</span>" +
                        "</li>");
                }
            }
        },
        error: function () {
            errorMessage();
        }
    });
}