package org.aissms.cicada.messaging;

import java.util.ArrayList;

import org.aissms.cicada.block.MessageBlock;
import org.aissms.cicada.block.MessageBlockRepository;
import org.aissms.cicada.conversation.Conversation;
import org.aissms.cicada.conversation.ConversationRepository;
import org.aissms.cicada.user.User;
import org.aissms.cicada.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired UserService userService;
    @Autowired ConversationRepository conversationRepository;
    @Autowired MessageBlockRepository blockRepository;
    @Autowired SimpMessagingTemplate messagingTemplate;

    public MyMessageDto storeMessage(String senderEmail, String receiverEmail, String content, String imageLink) {
        User receiver = userService.findByEmail(receiverEmail);
        if(receiver == null) return null;
        User sender = userService.findByEmail(senderEmail);

        MyMessage message = new MyMessage();
        message.setAuthorId(sender.getId());
        message.setContent(content);
        message.setTime();
        if(imageLink != null) message.setImageLink(imageLink);

        System.out.println(message.getImageLink());

        String convId = sender.getConversation().get(receiver.getId());
        Conversation conv = conversationRepository.findById(convId).get();

        int blockIndex = conv.size();

        MessageBlock block = blockRepository.findById(conv.getLastBlockId()).get();
        block.addMessage(message);
        blockRepository.save(block);

        int messageIndex = block.getMessageList().size();

        // if block becomes full create new block and add to conversation
        if(block.isFull()) {
            MessageBlock block2 = new MessageBlock();
            block2.setMessageList(new ArrayList<MyMessage>());
            blockRepository.save(block2);
            conv.addBlock(block2);
            conversationRepository.save(conv);
        }

        MyMessageDto dto = new MyMessageDto();
        dto.setAuthor(sender.getEmail());
        dto.setContent(content);
        dto.setDate(message.getTime());
        dto.setBlockIndex(blockIndex);
        dto.setImageLink(imageLink);
        dto.setMessageIndex(messageIndex);

        return dto;
    }

    public void notifyUser(String email, MyMessageDto messageDto) {
        messagingTemplate.convertAndSend("/messages/"+email, messageDto);
    }
    public void notifyUser(String email, String message) {
        messagingTemplate.convertAndSend("/messages/"+email, message);
    }
}
