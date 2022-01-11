package com.example.producer_consumer.websocket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private static  SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public static synchronized void  notifyFrontEnd(final String topicSuffix ) {
        messagingTemplate.convertAndSend("/topic/" , topicSuffix);
    }

}
