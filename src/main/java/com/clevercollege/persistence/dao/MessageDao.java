package com.clevercollege.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.clevercollege.model.Message;

public interface MessageDao {

    public List<Message> findAll() throws SQLException;

    public Message findByPrimaryKey(long id) throws SQLException;

    public void saveOrUpdate(Message message) throws SQLException;

    public void delete(long id) throws SQLException;

    public List<Message> findByUser(String cf) throws SQLException;

    public List<Message> findBySenderAndReceiver(String senderCf, String receiverCf) throws SQLException;

}
