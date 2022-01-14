package com.clevercollege.services;

import com.clevercollege.model.Location;
import com.clevercollege.model.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> webSocketSession = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("aaaaa");
        if(session.getAttributes().get("user") != null)
            webSocketSession.add(session);
        else {
            System.out.println("oooo");
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(message.getPayload(), Message.class);

        for(int i = 0; i < webSocketSession.size(); i++) {
            User u = (User) webSocketSession.get(i).getAttributes().get("user");
            if(u.getCf().equals(m.receiverCf)) {
                webSocketSession.get(i).sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSession.remove(session);
    }
}
