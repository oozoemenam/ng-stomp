package com.example.stomp;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public String send(@Payload String message, Principal principal) {
        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        System.out.println("SendToAll" + principal.getName() + name);
        return name;
    }

    @MessageMapping("/message")
    // @SendToUser("/queue/reply")
    public String processMessageFromClient(@Payload String message, Principal principal) {
        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        System.out.println("SendToUser" + principal.getName() + name + message + principal);
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply", name);
        return name;
    }

//    @MessageMapping("/message")
//    public void processMessageFromClient(@Payload String message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
//        System.out.println(sessionId);
//        headerAccessor.setSessionId(sessionId);
//        messagingTemplate.convertAndSend("/topic/reply", new Gson().fromJson(message, Map.class).get("name"));
//    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable throwable) {
        return throwable.getMessage();
    }
}
