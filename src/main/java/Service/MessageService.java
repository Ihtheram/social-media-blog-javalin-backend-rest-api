package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    /**
     * no args constructor
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    /**
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesForUser(int account_id){
        return messageDAO.getAllMessagesForUser(account_id);
    }
    
    public Message getMessageByMessageId(int message_id){
        return messageDAO.getMessageByMessageId(message_id);
    }

    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    public Message deleteMessage(int message_id){
        return messageDAO.deleteMessage(message_id);
    }

    public Message updateMessage(int message_id, Message message){
        return messageDAO.updateMessage(message_id, message);
    }
}
