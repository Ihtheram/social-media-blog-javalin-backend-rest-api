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

        // Account authentication endpoints
        app.post("/register", this::registrationHandler);
        app.post("/login", this::loginHandler);
        
        // Message query and manipulation endpoints
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
                  
        return app;
    }

    /**
     * Handler to Register an Account
     * @param context, contains a JSON of Account (with username, password) except account_id
     * @throws JsonProcessingException if failure occur converting JSON into an object.
     * @response if successful, JSON of Account (with account_id, username, password) and status=200 Ok
     *           if fails, status=400 (Client error)
     */
    private void registrationHandler(Context context) throws JsonProcessingException{

        ObjectMapper objMapper = new ObjectMapper();
        Account account = objMapper.readValue(context.body(), Account.class);

        Account register = accountService.register(account);
       
        if(register==null){
            context.status(400); // if registration fails
        }else{
            context.json(objMapper.writeValueAsString(register)); // if registration is successful. Status is 200.
        }
    }

    /**
     * Handler to Log into an Account
     * @param context, contains a JSON of Account (with username, password) except account_id.
     * @throws JsonProcessingException if failure occur converting JSON into an object.
     * @response if successful- JSON of Account (with account_id, username, password) and status=200(Ok).
     *           if fails- status=401(Unauthorized).
     */
    private void loginHandler(Context context) throws JsonProcessingException{

        ObjectMapper objMapper = new ObjectMapper();
        Account account = objMapper.readValue(context.body(), Account.class);

        Account login = accountService.login(account);

        if(login==null){
            context.status(401);  // if login fails
        }else{
            context.json(objMapper.writeValueAsString(login)); // if login is successful. Status is 200.
        }  
    }

    /**
     * Handler to Post a Message
     * @param context, contains a representation of a JSON of Message (with posted_by, message_text, time_posted_epoch) except message_id
     * @throws JsonProcessingException if failure occur converting JSON into an object.
     * @response if successful, JSON of Message (with message_id, posted_by, message_text, time_posted_epoch), status=200 Ok.
     *           if fails, status=400 (Client error)
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper writeobjectMapper = new ObjectMapper();
        
        Message message = objectMapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        
        if(addedMessage!=null){
            context.json(writeobjectMapper.writeValueAsString(addedMessage));
        }else{
            context.status(400);
        }
    }

    /**
     * Handler to Get All Messages
     * @response JSON of a List of either all messages retrieved from the database
     * or empty if there are no messages, status=200 (always).
     */
    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

    /**
     * Handler to Get a Message by Message ID
     * @param context, contains an integer message_id within its path parameter
     * @response JSON of a Message object with matched ID retrieved from the database
     * or empty if there is no matching message, status=200 (always).
     */
    private void getMessageByMessageIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message foundMessage = messageService.getMessageByMessageId(message_id);
        
        if(foundMessage!=null){
            context.json(messageService.getMessageByMessageId(message_id));
        }else{
            context.status(200);
        }   
    }

    /**
     * Handler to Delete a Message Identified by Message ID
     * @param context, contains an integer message_id within its path parameter
     * @response JSON of a Message object which matched ID if deleted from the database
     * or empty if there is no matching message to be deleted, status=200 (always).
     */
    private void deleteMessageHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message foundMessage = messageService.getMessageByMessageId(message_id);
        
        if(foundMessage!=null){
            context.json(messageService.deleteMessage(message_id));
        }else{
            context.status(200);
        }   
    }

    /**
     * Handler to Update a Message Text Identified by Message ID
     * @param context, contains an integer message_id within its path parameter
     * @response JSON of a Message object which matched ID if successfully updated and status 200,
     *           if update unsuccessful, status=400.
     */
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        
        Message updatedMessage = messageService.updateMessage(message_id, message);
        
        if(updatedMessage==null){
            context.status(400);
        }else{
            context.json(objectMapper.writeValueAsString(updatedMessage));
        } 
    }

    /**
     * Handler to Get All Messages of A Particular User
     * @param context, contains an integer account_id within its path parameter
     * @response JSON of a List of all Messages of the particular user
     * or empty List if there are no messages, status=200 (always).
     */
    private void getAllMessagesForUserHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllMessagesForUser(account_id));
    }

}