package org.aissms.cicada.messaging;

import org.aissms.cicada.conversation.Conversation;
import org.aissms.cicada.conversation.ConversationService;
import org.aissms.cicada.mongo.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


// send file with part name as "image"
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/image")
public class ImageController {
    @Autowired MessageService messageService;
    @Autowired FileService fileService;
    @Autowired ConversationService conversationService;

    @PostMapping("/send/{email}")
    public void sendImage(@PathVariable("email") String email2, MultipartFile image, Authentication auth) {
        Conversation conv = conversationService.getConversationWith(auth.getName(), email2);
        // invalid request
        if(conv == null) {
            return;
        }
        if(image == null) System.out.println("IMAGE NULL");
        String url = fileService.storeFile(image);
        MyMessageDto dto = messageService.storeMessage(auth.getName(), email2, null, url);
        messageService.notifyUser(email2, dto);
    }
}
