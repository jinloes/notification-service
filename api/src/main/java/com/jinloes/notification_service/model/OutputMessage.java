package com.jinloes.notification_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputMessage {
    private final String message;

    @JsonCreator
    public OutputMessage(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

