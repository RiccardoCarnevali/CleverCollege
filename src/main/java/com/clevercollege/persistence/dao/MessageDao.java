package com.clevercollege.persistence.dao;

import com.clevercollege.model.Message;
import com.clevercollege.model.User;

import java.sql.SQLException;
import java.util.List;

public interface MessageDao {

    public List<Message> findAll() throws SQLException;

    public Message findByPrimaryKey(long id) throws SQLException;

    public void saveOrUpdate(Message message) throws SQLException;

    public void delete(long id) throws SQLException;

    public List<Message> findByUser(String cf) throws SQLException;

    public List<Message> findBySenderAndReceiver(String senderCf, String receiverCf) throws SQLException;

}
