package com.clevercollege.model;

public class Message {
    Long id;
    String senderCf;
    String receiverCf;
    String textMessage;

    public Message() {}

    public Message(Long id, String senderCf, String receiverCf, String textMessage) {
        this.id = id;
        this.senderCf = senderCf;
        this.receiverCf = receiverCf;
        this.textMessage = textMessage;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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
