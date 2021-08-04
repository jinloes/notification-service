package com.jinloes.notification_service.web;

import com.jinloes.notification_service.model.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Controller
public class AnnouncementController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MessageMapping("/announcements")
    //@SendTo("/topic/announcements")
    public void send(Message message) throws Exception {

        kafkaTemplate.send("test", new OutputMessage("Hi!"))
        .addCallback(new SuccessCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                System.out.println("Success!");
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Fail!");
            }
        });
    }

    @MessageMapping("/message/{userId}")
    //@SendTo("/topic/announcements")
    public void send(@DestinationVariable String userId, Message message) throws Exception {

        kafkaTemplate.send("test-"+ userId, new OutputMessage("Hi " + userId))
                .addCallback(new SuccessCallback<SendResult<String, Object>>() {
                    @Override
                    public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                        System.out.println("Success!");
                    }
                }, new FailureCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("Fail!");
                    }
                });
    }
}
