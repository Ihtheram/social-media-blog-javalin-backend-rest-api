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
     * Insert a Message
     * @param message, a Message object
     * @return the new message object if creation in database is successful otherwise returns null
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            /*
             * CHECK IF
             * message_text is not blank,
             * message_text is under 255 characters,
             */
            if (message.getMessage_text().length()>0 && message.getMessage_text().length()<255)
                preparedStatement.executeUpdate();
            ResultSet ResultSetwPKey = preparedStatement.getGeneratedKeys();
            if(ResultSetwPKey.next()){
                int genPKey_message_id = ResultSetwPKey.getInt(1);
                return new Message(genPKey_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get All Messages Available
     * @return a List of all messages if there are or empty List if none
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

    /**
     * Get a Particular Message by Message ID
     * @param message_id, a Message ID
     * @return the matching Message object if exists, null otherwise
     */
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

    /**
     * Delete a Particular Message Identified by Message ID
     * @param message_id, a Message ID
     * @return the matching Message object if exists and successfully deleted, null otherwise
     */
    public Message deleteMessage(int message_id){
        
        Message toDelete = getMessageByMessageId(message_id);
        
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            int rowaffected = preparedStatement.executeUpdate();
            
            if(rowaffected>0)
                return toDelete;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Update a Particular Message Text Identified by Message ID
     * @param message_id, an integer message ID
     * @param message, a Message object containing message text
     * @return the Message object if exists and successfully updated, null otherwise
     */
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

    /**
     * Get All Messages of a Particular User
     * @param account_id, an integer account ID
     * @return the List of availlable Messages of user if any exist, empty List otherwise
     */
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
    
}
