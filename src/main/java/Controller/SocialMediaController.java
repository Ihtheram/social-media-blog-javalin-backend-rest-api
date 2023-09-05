package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        messageService = new MessageService();
        accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {

        Javalin app = Javalin.create();

        app.post("/login", this::loginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.post("/messages", this::postMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
            
        return app;
    }

    private void loginHandler(Context context) throws JsonProcessingException{

        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(context.body(), Account.class);

        Account login = accountService.login(account);
        if(login==null){
            context.status(401);
        }
        else{
            context.json(objectMapper.writeValueAsString(login));
        }
         
    }

    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

    private void getMessageByMessageIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message foundMessage = messageService.getMessageByMessageId(message_id);
        if(foundMessage!=null){
            context.json(messageService.getMessageByMessageId(message_id));
        }else{
            context.status(200);
        }   
    }

    private void getAllMessagesForUserHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllMessagesForUser(account_id));
    }



    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        Message message = objectMapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        
        if(addedMessage!=null){
            context.json(objectMapper.writeValueAsString(addedMessage));
        }else{
            context.status(400);
        }
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        
        Message updatedMessage = messageService.updateMessage(message_id, message);
        if(updatedMessage==null){
            context.status(400);
        }
        else{
            context.json(objectMapper.writeValueAsString(updatedMessage));
        }
        
    }

    private void deleteMessageHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message foundMessage = messageService.getMessageByMessageId(message_id);
        if(foundMessage!=null){
            context.json(messageService.deleteMessage(message_id));
        }else{
            context.status(200);
        }   
    }


}