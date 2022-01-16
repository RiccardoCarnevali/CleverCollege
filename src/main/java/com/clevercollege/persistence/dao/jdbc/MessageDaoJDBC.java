package com.clevercollege.persistence.dao.jdbc;

import com.clevercollege.model.Message;
import com.clevercollege.persistence.dao.MessageDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoJDBC implements MessageDao {

    private Connection conn;

    public MessageDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Message> findAll() throws SQLException {
        List<Message> messages = new ArrayList<>();

        String query = "select * from messages order by id";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setSenderCf(rs.getString("cf_sender"));
            message.setReceiverCf(rs.getString("cf_receiver"));
            message.setTextMessage(rs.getString("text_message"));
        }

        return messages;
    }

    @Override
    public Message findByPrimaryKey(long id) throws SQLException {
        Message message = new Message();

        String query = "select * from messages where id = ?";

        PreparedStatement st = conn.prepareStatement(query);
        st.setLong(1, id);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            message.setId(rs.getLong("id"));
            message.setSenderCf(rs.getString("cf_sender"));
            message.setReceiverCf(rs.getString("cf_receiver"));
            message.setTextMessage(rs.getString("text_message"));
        }

        return message;
    }

    @Override
    public void saveOrUpdate(Message message) throws SQLException {
        String query = "select * from messages where id = ?";

        PreparedStatement st = conn.prepareStatement(query);

        st.setLong(1, message.getId());

        ResultSet rs = st.executeQuery();

        if(rs.next()) {

            query = "update messages set "
                    + "id = ?,"
                    + "cf_sender = ?,"
                    + "cf_receiver = ?,"
                    + "text_message = ?";

            PreparedStatement updateSt = conn.prepareStatement(query);

            updateSt.setLong(1, message.getId());
            updateSt.setString(2, message.getSenderCf());
            updateSt.setString(3, message.getReceiverCf());
            updateSt.setString(4, message.getTextMessage());

            updateSt.executeUpdate();

        }
        else {

            query = "insert into messages values(?,?,?,?)";

            PreparedStatement insertSt = conn.prepareStatement(query);

            insertSt.setString(1, message.getSenderCf());
            insertSt.setString(2, message.getReceiverCf());
            insertSt.setString(3, message.getTextMessage());
            insertSt.setLong(4, message.getId());

            insertSt.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        String query = "delete from messages where id = ?";

        PreparedStatement st = conn.prepareStatement(query);

        st.setLong(1, id);

        st.executeUpdate();
    }

    @Override
    public List<Message> findByUser(String cf) throws SQLException {
        List<Message> messages = new ArrayList<>();

        String query = "select * from messages as m1 where (m1.cf_sender = ? or " +
                "m1.cf_receiver = ? ) and m1.id >= all (select m2.id from messages as m2 " +
                "where (m1.cf_sender = m2.cf_receiver and m1.cf_receiver = m2.cf_sender) or (m1.cf_sender " +
                "= m2.cf_sender and m1.cf_receiver = m2.cf_receiver))";

        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, cf);
        st.setString(2, cf);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setSenderCf(rs.getString("cf_sender"));
            message.setReceiverCf(rs.getString("cf_receiver"));
            message.setTextMessage(rs.getString("text_message"));
            messages.add(message);
        }

        return messages;
    }

    @Override
    public List<Message> findBySenderAndReceiver(String senderCf, String receiverCf) throws SQLException {
        System.out.println(senderCf);
        System.out.println(receiverCf);
        List<Message> messages = new ArrayList<>();

        String query = "select * from messages where (cf_receiver = ? and cf_sender = ?) or (cf_sender = ? and cf_receiver = ?)";

        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, senderCf);
        st.setString(2, receiverCf);
        st.setString(3, senderCf);
        st.setString(4, receiverCf);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setSenderCf(rs.getString("cf_sender"));
            message.setReceiverCf(rs.getString("cf_receiver"));
            message.setTextMessage(rs.getString("text_message"));
            messages.add(message);
        }

        return messages;
    }
}
