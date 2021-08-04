package com.jinloes.notification_service.messaging;

import com.jinloes.notification_service.model.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(topics = "test", groupId = "1 ")
    public void listen(Message<OutputMessage> message) {
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/announcements", message.getPayload());
    }

    @KafkaListener(topicPattern = "test-.*", groupId = "1 ")
    public void listenUser(Message<OutputMessage> message) {
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/message/123" , message.getPayload());
    }
}