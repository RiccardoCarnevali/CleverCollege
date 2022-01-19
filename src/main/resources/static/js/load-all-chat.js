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
		  if(response.length == 0)
		  	  $(".list-group").append("<li class='list-group-item' style='text-align: center; margin: 10px 0px'>Nessuna chat trovata</li>")
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
			   image = tmpChattingUser.profilePicture == null ? "/assets/images/pp/pp-placeholder.png" : "/assets/images/pp/"+ tmpChattingUser.profilePicture;
               $(".list-group").append("<li class='list-group-item user'>" +
                   "<div class='profile-picture-container'>" +
                   "<img class='profile-picture rounded-circle' src='" + image + "'>" +
                   "</div>" +
                   "<div id='center'>" +
                   "<span class='user-name'>" + tmpChattingUser.firstName + " " + tmpChattingUser.lastName + "</span>" +
                   "<span class='professor-name' style='display:block'>" + textMessage + "</span>" +
                   "</div>" +
                   "<div class='icons'>" +
                   "<a href='/allMyChat/singleChat?cfUser=" + tmpChattingUser.cf + "' class='far fa-comment chat-button'></a>" +
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