package com.jinloes.notification_service.web;

import com.jinloes.notification_service.model.OutputMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AnnouncementController {

    @MessageMapping("/announcements")
    @SendTo("/topic/announcements")
    public OutputMessage send(Message message) throws Exception {
        return new OutputMessage("test");
    }
}
