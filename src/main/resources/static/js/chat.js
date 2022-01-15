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
            "<li class='single-messages' style='margin-top: 10px'>"+ message.textMessage +
            "</div></li>");
    };

    $("#sendMessage").on('click', function () {
        $(".all-messages").append("<div class='send-message'>" +
            "<li class='single-messages' style='margin-top: 10px; margin-right: 10px'>"+ $('#message-input').val() +
            "</div></li>");
        websocket.send(JSON.stringify(new Message(null, cfSender, cfReceiver, $('#message-input').val())));
        $("#message-input").val("");
    });

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
            else
                document.getElementById('information-about-receiver').innerHTML = (response.firstName + " " + response.lastName).toUpperCase();
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
            cfSender : cfSender,
            cfReceiver : cfReceiver
        },
        success: function (response) {
            if(response == null)
                errorMessage();
            for(let i = 0; i < response.length; i++) {
                if(response[i].senderCf === cfSender)
                    $(".all-messages").append("<div class='send-message'>" +
                        "<li class='single-messages' style='margin-top: 10px; margin-right: 10px'>"+ response[i].textMessage +
                        "</div></li>");
                else
                    $(".all-messages").append("<div>" +
                        "<li class='single-messages' style='margin-top: 10px'>"+ response[i].textMessage +
                        "</div></li>");
            }
        },
        error: function () {
            errorMessage();
        }
    });
}

function Message(id, senderCf, receiverCf, textMessage) {
    this.id = id;
    this.senderCf = senderCf;
    this.receiverCf = receiverCf;
    this.textMessage = textMessage;
}