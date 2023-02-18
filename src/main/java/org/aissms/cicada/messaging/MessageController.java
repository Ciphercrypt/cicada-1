package org.aissms.cicada.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {
    @Autowired MessageService messageService;

    @MessageMapping("/send/{email}")
    public void sendMessage(@Payload String message, @DestinationVariable("email") String email, Authentication auth) {
        
        MyMessageDto dto = messageService.storeMessage(auth.getName(), email, message);
        
        // if dto is null don't send message to receiver
        if(dto == null) {
            return;
        }

        // notify user
        messageService.notifyUser(email, dto);

    }
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
