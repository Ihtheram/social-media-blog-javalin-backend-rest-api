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

    public List<Message> getAllMessagesByMessageId(int message_id){
        return messageDAO.getAllMessagesByMessageId(message_id);
    }

    public Message addMessage(Message message) {
        if(message.equals(messageDAO.getMessageByMessageId(message.getMessage_id()))){
            return null;
        };
        return messageDAO.insertMessage(message);
    }
}
