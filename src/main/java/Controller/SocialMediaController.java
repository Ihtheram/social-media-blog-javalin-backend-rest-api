package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Message;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;

    public SocialMediaController(){
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {

        Javalin app = Javalin.create();

        app.get("/messages", this::getAllMessagesHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByMessageIdHandler);
        app.post("/messages", this::postMessageHandler);
        
        return app;
    }

    /**
     * @param context
     */
    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

    /**
     * @param context
     */
    private void getAllMessagesByMessageIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllMessagesByMessageId(message_id));
    }


    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            context.json(mapper.writeValueAsString(addedMessage));
        }else{
            context.status(400);
        }
    }



}