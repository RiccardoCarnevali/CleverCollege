package com.clevercollege.controller;

import com.clevercollege.model.Message;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ChatController {

    @PostMapping("loadAllChat")
    public List<Message> loadAllMessages(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null)
            return null;

        try {
            List<Message> allMessages = DatabaseManager.getInstance().getMessageDao().findByUser(u.getCf());
            return allMessages;
        } catch (SQLException e) {
            return null;
        }
    }

    @PostMapping("loadChattingUser")
    public User loadUser(HttpServletRequest request, String cfUser) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null || cfUser == null) {
            return null;
        }

        try {
            User chattingUser = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cfUser);
            return chattingUser;
        } catch (SQLException e) {
            return null;
        }
    }

    @PostMapping("loadMessages")
    public List<Message> loadMessages(HttpServletRequest request, String cfReceiver) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null || cfReceiver == null) {
            return null;
        }

        try {
            List<Message> messageList = DatabaseManager.getInstance().getMessageDao().findBySenderAndReceiver(u.getCf(), cfReceiver);
            return messageList;
        } catch (SQLException e) {
            return null;
        }
    }	
}
