package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {


    /**
     * @return all messages
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageByMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    public List<Message> getAllMessagesForUser(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
                
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        /* Retrieving from database if there is any matching account ID */
            
        try {
            String sql1 = "SELECT account_id FROM account WHERE account_id = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, message.getPosted_by());
            ResultSet rs1 = ps1.executeQuery();
            int userId = rs1.getInt("account_id");
            
            
         
           
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            /**check if
             * message_text is not blank,
             * message_text is under 255 characters,
             * posted_by refers to existing user
             */     
            if (message.getMessage_text().length()>0 && message.getMessage_text().length()<255 && userId>=0
                    && userId==message.getPosted_by()) {
                preparedStatement.executeUpdate();

                ResultSet rs = preparedStatement.getGeneratedKeys();
                return getMessageByMessageId((int) rs.getInt("message_id"));
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());           
        }
        return null;
    }

    public Message deleteMessage(int message_id){
        
        Message toDelete = getMessageByMessageId(message_id);
        
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            preparedStatement.executeUpdate();

            return toDelete;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int message_id, Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            Message currentMsg = getMessageByMessageId(message_id);

            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message_id);
            
            /*
             * Check if-
             * the message id already exists,
             * the new message_text is not blank,
             * message_text is not over 255 characters
             */
            if(currentMsg!=null && message.message_text.length()>0 && message.message_text.length()<255){
                preparedStatement.executeUpdate();
                return getMessageByMessageId(message_id);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
