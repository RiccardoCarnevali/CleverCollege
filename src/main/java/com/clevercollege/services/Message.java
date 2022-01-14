package com.clevercollege.services;

public class Message {
    String senderCf;
    String receiverCf;
    String textMessage;

    public Message() {}

    public Message(String senderCf, String receiverCf, String textMessage) {
        this.senderCf = senderCf;
        this.receiverCf = receiverCf;
        this.textMessage = textMessage;
    }

    public String getSenderCf() {
        return senderCf;
    }

    public void setSenderCf(String senderCf) {
        this.senderCf = senderCf;
    }

    public String getReceiverCf() {
        return receiverCf;
    }

    public void setReceiverCf(String receiverCf) {
        this.receiverCf = receiverCf;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
