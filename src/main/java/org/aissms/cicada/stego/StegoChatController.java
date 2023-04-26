package org.aissms.cicada.stego;

import org.aissms.cicada.mongo.FileService;
import org.aissms.cicada.status.Status;
import org.aissms.cicada.status.StatusService;
import org.aissms.cicada.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/stego")
public class StegoChatController {
    
    @Autowired private StatusService statusService;
    @Autowired private StegoChatService stegoChatService;
    @Autowired private FileService fileService;
    @Autowired private CallRepository callRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    // if email2 is online and available returns ring end time
    // ring end time is when email2 is stopped ringing
    // ringEndTime is in milliseconds
    @GetMapping("/request/call/{email2}")
    public ResponseEntity<String> sendChatRequest(Authentication auth, @PathVariable("email2") String email2) {
        if(Status.OFFLINE.equals(statusService.getStatus(auth, email2))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is offline");
        }
        if(stegoChatService.busy(email2)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is on another call");
        }
        long ringEndTime = stegoChatService.ringUser(auth.getName(), email2);
        return ResponseEntity.ok(ringEndTime+"");
    }
    @PostMapping("/request/reject")
    public void rejectChatRequest(Authentication auth) {
        stegoChatService.rejectCall(auth.getName());
    }
    @PostMapping("/request/accept")
    public ResponseEntity<UserDto> acceptCallRequest(Authentication auth) {
        UserDto userDto = stegoChatService.acceptCall(auth.getName());
        if(userDto == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/call/end")
    public ResponseEntity<String> endCall(Authentication auth) {
        stegoChatService.endCall(auth.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadStegoImage(MultipartFile image, Authentication auth) {
        Call call = callRepository.getCall(auth.getName());
        if(call == null || call.alreadyUploaded(auth.getName())) return ResponseEntity.badRequest().build();

        String url = fileService.storeFile(image);
        call.setImageLink(auth.getName(), url);

        if(call.imagesReady()) {
            notifyUsers(call);
        }
        return ResponseEntity.ok().build();
    }
    private void notifyUsers(Call call) {
        messagingTemplate.convertAndSend("/messages/"+call.getEmail1(), new StegoFile(call.getImageLinkFromEmail2()));
        messagingTemplate.convertAndSend("/messages/"+call.getEmail2(), new StegoFile(call.getImageLinkFromEmail1()));
    }
}
