var cfSender = null;
var cfReceiver = null;

$(document).ready(function () {

    var messagesInformation = document.getElementsByClassName("all-messages")[0].id;
    cfSender = messagesInformation.substr(9, 25);
    cfReceiver = messagesInformation.substr(26, messagesInformation.length);

    const websocket = new WebSocket("ws://localhost:8080/chatSocket");
    websocket.onmessage = (event) => {
        var message = JSON.parse(event.data);
        $(".messages").append("<li class='single-messages' style='float:right; margin-top: 10px'>"+ message.text + "</li>");
    };

    $("#sendMessage").on('click', function () {
        $(".all-messages").append("<li class='single-messages' style='margin-top: 10px;'>"+ $('#message-input').val() + "</li>");
        $("#message-input").val("");
        websocket.send(JSON.stringify(new Message(cfSender, cfReceiver, $('#message-input').val())));
    });

});

function Message(senderCf, receiverCf, textMessage) {
    this.senderCf = senderCf;
    this.receiverCf = receiverCf;
    this.textMessage = textMessage;
}