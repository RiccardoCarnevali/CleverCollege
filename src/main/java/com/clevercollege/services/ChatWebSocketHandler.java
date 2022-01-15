package com.clevercollege.services;

import com.clevercollege.model.Message;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> webSocketSession = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(session.getAttributes().get("user") != null)
            webSocketSession.add(session);
        else {
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(message.getPayload(), Message.class);
        m.setId(DatabaseManager.getInstance().getIdBroker().getNextMessageId());
        DatabaseManager.getInstance().getMessageDao().saveOrUpdate(m);
        DatabaseManager.getInstance().commit();
        for(int i = 0; i < webSocketSession.size(); i++) {
            User u = (User) webSocketSession.get(i).getAttributes().get("user");
            if(u.getCf().equals(m.getReceiverCf())) {
                webSocketSession.get(i).sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSession.remove(session);
    }
}
