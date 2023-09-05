package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    /**
     * Constructor, no args
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * Constructor, parameterized 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    /**
     * Creating Message
     * @param message, a Message object
     * @return either the message object if inserted in database or null if message creation fails
     */
    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    /**
     * Getting All Messages
     * @return a List of all messages exist or empty List if non exists.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Getting a Particular Message
     * @param message_id, an integer ID of the Message to be retrieved
     * @return the matching Message object or null if no match.
     */
    public Message getMessageByMessageId(int message_id){
        return messageDAO.getMessageByMessageId(message_id);
    }

    /**
     * Deleting a Particular Message
     * @param message_id, an integer ID of the Message to be deleted
     * @return the deleted Message's object or null if there's no matching message to be deleted.
     */
    public Message deleteMessage(int message_id){
        return messageDAO.deleteMessage(message_id);
    }

    /**
     * Updating a Particular Message's Text
     * @param message_id, an integer ID of the Message to be updated
     * @param message, a Message object which contains message text
     * @return the updated Message object or null if updating is not successful.
     */
    public Message updateMessage(int message_id, Message message){
        return messageDAO.updateMessage(message_id, message);
    }
    
    /**
     * Getting All Messages of a Particular User
     * @param account_id, an integer ID of a user's account
     * @return a List of all messages exist or empty List if non exists.
     */
     public List<Message> getAllMessagesForUser(int account_id){
        return messageDAO.getAllMessagesForUser(account_id);
    }


}
