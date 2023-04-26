package org.aissms.cicada.block;

import java.util.ArrayList;
import java.util.List;

import org.aissms.cicada.conversation.Conversation;
import org.aissms.cicada.conversation.ConversationRepository;
import org.aissms.cicada.messaging.MyMessage;
import org.aissms.cicada.messaging.MyMessageDto;
import org.aissms.cicada.mongo.FileController;
import org.aissms.cicada.user.User;
import org.aissms.cicada.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageBlockService {
    @Autowired ConversationRepository conversationRepository;
    @Autowired MessageBlockRepository blockRepository;
    @Autowired UserService userService;

    public int getBlockCount(String email1, String email2) {
        User user1 = userService.findByEmail(email1);
        User user2 = userService.findByEmail(email2);
        
        if(user1 == null || user2 == null) return -1;

        String convId = user1.getConversation().get(user2.getId());
        if(convId == null) return -1;

        Conversation conversation = conversationRepository.findById(convId).get();
        return conversation.size();
    }

    // // blocks are 1 indexed
    // public MessageBlock getMessageBlock(String email1, String email2, int index) {
    //     if(index <= 0) return null;
        
    //     Conversation conv = service.getConversationWith(email1, email2);
    //     if(conv == null) return null;

    //     if(index > conv.size()) return null;

    //     return repository.findById(conv.getBlockId(index-1)).get();
    // }
    // // get last block
    // public MessageBlock getLastMessageBlock(String email1, String email2) {
    //     Conversation conv = service.getConversationWith(email1, email2);
    //     if(conv == null) return null;

    //     return repository.findById(conv.getLastBlockId()).get();
    // }
    // get as dto
    public MessageBlockDto getMessageBlockDto(String email1, String email2, int index) {
        if(index <= 0) return null;
        User user1 = userService.findByEmail(email1);
        User user2 = userService.findByEmail(email2);
        
        if(user1 == null || user2 == null) return null;

        String convId = user1.getConversation().get(user2.getId());
        if(convId == null) return null;

        Conversation conversation = conversationRepository.findById(convId).get();
        if(index > conversation.size()) return null;

        MessageBlock block = blockRepository.findById(conversation.getBlockId(index-1)).get();
        return convert(index, user1, user2, block);
    }
    private MessageBlockDto convert(int index, User user1, User user2, MessageBlock block) {
        MessageBlockDto dto = new MessageBlockDto();
        dto.blockIndex = index;
        List<MyMessageDto> messageDtoList = new ArrayList<MyMessageDto>();
        int messageIndex = 1;
        for(MyMessage message : block.getMessageList()) {
            MyMessageDto obj = new MyMessageDto();
            obj.setBlockIndex(index);
            obj.setContent(message.getContent());
            obj.setDate(message.getTime());
            obj.setMessageIndex(messageIndex);
            if(message.getImageLink() != null) {
                obj.setImageLink(FileController.REQUEST_PATH+message.getImageLink());
                obj.setMessagetype("FILE");
            }
            if(message.getAuthorId().equals(user1.getId())) {
                obj.setAuthor(user1.getEmail());
            } else {
                obj.setAuthor(user2.getEmail());
            }
            messageDtoList.add(obj);
            messageIndex++;
        }
        dto.messageList = messageDtoList;
        return dto;
    }
    public MyMessageDto getLastMessage(String email1, String email2) {
        User user1 = userService.findByEmail(email1);
        User user2 = userService.findByEmail(email2);
        
        if(user1 == null || user2 == null) return null;

        String convId = user1.getConversation().get(user2.getId());
        if(convId == null) return null;

        Conversation conversation = conversationRepository.findById(convId).get();
        int blockCount = conversation.size();
        MessageBlockDto dto = getMessageBlockDto(email1, email2, blockCount);
        List<MyMessageDto> messageList = dto.messageList;
        if(messageList.isEmpty()) return null;
        return messageList.get(messageList.size()-1);
    }
}
