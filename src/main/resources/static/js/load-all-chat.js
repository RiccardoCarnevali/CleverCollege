var tmpChattingUser = null;
var myUser = null;

$(document).ready(function () {
    var userInformation = document.getElementsByClassName("list-group")[0].id;
    myUser = userInformation.substr(9);
    loadAllExistingChat();
});

var loadAllExistingChat = function () {
  $.ajax({
      url:"/loadAllChat",
      method: "POST",
      success: function (response) {
          if(response === null)
              errorMessage();
          for(let i = 0; i < response.length; i++) {
              if (response[i].senderCf !== myUser) {
                  loadChattingUser(response[i].senderCf, response[i].textMessage);
              } else
                  loadChattingUser(response[i].receiverCf, response[i].textMessage);
          }
      },
      error: function () {
          errorMessage();
      }
  });
};

var loadChattingUser = function (cfUser, textMessage) {
    $.ajax({
       url:"/loadChattingUser",
        method: "POST",
        data: {
           cfUser : cfUser
        },
        success: function (response) {
           if(response === null)
               errorMessage();
           else {
               tmpChattingUser = response;
               $(".list-group").append("<li class='list-group-item user'>" +
                   "<div id='profile-picture-container'>" +
                   "<img id='profile-picture' class='rounded-circle' src='../assets/images/pp/"+ tmpChattingUser.profilePicture +"'>" +
                   "</div>" +
                   "<div id='center'>" +
                   "<span class='user-name'>" + tmpChattingUser.firstName + " " + tmpChattingUser.lastName + "</span>" +
                   "<span class='professor-name' style='display:block'>" + textMessage + "</span>" +
                   "</div>" +
                   "<div class='icons'>" +
                   "<form id='buttonSendMessContainer' action='/allMyChat/singleChat' method='get'>" +
                   "<button id='sendMessageToProf' name='cfUser' value="+ tmpChattingUser.cf +"> </button>" +
                   "</form>" +
                   "</div>" +
                   "</li>");
           }
        },
        error: function () {
            errorMessage();
        }
    });
}