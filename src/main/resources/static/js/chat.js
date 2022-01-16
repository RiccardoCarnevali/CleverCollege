var cfSender = null;
var cfReceiver = null;

$(document).ready(function () {
	
    var messagesInformation = document.getElementsByClassName("all-messages")[0].id;
    cfSender = messagesInformation.substr(9, 16);
    cfReceiver = messagesInformation.substr(26);
    loadInformationAboutReceiver(cfReceiver);

    const websocket = new WebSocket("ws://localhost:8080/chatSocket");
    websocket.onmessage = (event) => {
        var message = JSON.parse(event.data);
        $(".all-messages").append("<div>" +
            "<li class='single-messages' style='margin: 10px 100px 0px 10px'>"+ message.textMessage +
            "</div></li>");
		$('.all-messages').scrollTop($('.all-messages')[0].scrollHeight);
    };

    $("#sendMessage").on('click', function () {
        $(".all-messages").append("<div class='send-message'>" +
            "<li class='single-messages' style='margin: 10px 10px 0px 100px; background-color: #e2dded'>"+ $('#message-input').val() +
            "</div></li>");
        websocket.send(JSON.stringify(new Message(null, cfSender, cfReceiver, $('#message-input').val())));
        $("#message-input").val("");
    });

	var shiftDown = false;

	$(document).keypress(function(e) {
		if(e.keyCode == 13) {
			if($("#message-input").is(":focus") && !shiftDown) {
				e.preventDefault();
				$(".all-messages").append("<div class='send-message'>" +
		            "<li class='single-messages' style='margin: 10px 10px 0px 100px; background-color: #e2dded'>"+ $('#message-input').val() +
		            "</div></li>");
		        websocket.send(JSON.stringify(new Message(null, cfSender, cfReceiver, $('#message-input').val())));
		        $("#message-input").val("");
			}
		}
	});
	
	$(document).keydown(function(e) {
		if(e.keyCode == 16) shiftDown = true;
	});
	
	$(document).keyup(function(e) {
		if(e.keyCode == 16) shiftDown = false;
	})

    loadMessages(cfSender, cfReceiver);
});

var loadInformationAboutReceiver = function (cfReceiver) {
    $.ajax({
        url:"/loadChattingUser",
        method: "POST",
        data: {
            cfUser : cfReceiver
        },
        success: function (response) {
            if(response === null)
                errorMessage();
            else {
				image = response.profilePicture == null ? "/assets/images/pp-placeholder.png" : "/assets/images/pp/"+ response.profilePicture;
                document.getElementById('information-about-receiver').innerHTML = "<img class='profile-picture rounded-circle' src='" + image + "' style='margin-right: 10px'>" + response.firstName.toUpperCase() + " " + response.lastName.toUpperCase()
			}
        },
        error: function () {
            errorMessage();
        }
    });
}

var loadMessages = function (cfSender, cfReceiver) {
    $.ajax({
        url:"/loadMessages",
        method: "POST",
        data: {
            cfReceiver : cfReceiver
        },
        success: function (response) {
            if(response == null)
                errorMessage();
            for(let i = 0; i < response.length; i++) {
                if(response[i].senderCf === cfSender)
                    $(".all-messages").append("<div class='send-message'>" +
                        "<li class='single-messages' style='margin: 10px 10px 0px 100px; background-color: #e2dded'>"+ response[i].textMessage +
                        "</div></li>");
                else
                    $(".all-messages").append("<div>" +
                        "<li class='single-messages' style='margin: 10px 100px 0px 10px'>"+ response[i].textMessage +
                        "</div></li>");
            }
			$('.all-messages').scrollTop($('.all-messages')[0].scrollHeight);
        },
        error: function () {
            errorMessage();
        }
    });
}

class Message {
    constructor(id, senderCf, receiverCf, textMessage) {
        this.id = id;
        this.senderCf = senderCf;
        this.receiverCf = receiverCf;
        this.textMessage = textMessage;
    }
}