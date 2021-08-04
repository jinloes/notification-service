package com.jinloes.notification_service;

import com.jinloes.notification_service.model.OutputMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnnouncementControllerTest {
    @Value("${local.server.port}")
    private int port;

    private WebSocketStompClient stompClient;
    private String url;
    private CompletableFuture<OutputMessage> completableFuture;

    @Before
    public void setUp() throws Exception {
        url = "ws://localhost:" + port + "/notifications";
        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        completableFuture = new CompletableFuture<>();
    }

    @Test
    public void testSendNotification() throws InterruptedException, ExecutionException, TimeoutException {

        StompSession stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/announcements", new AnnouncementFrameHandler());
        stompSession.send("/app/announcements", new OutputMessage("hi!"));
        OutputMessage gameStateAfterMove = completableFuture.get(5, SECONDS);

        assertThat(gameStateAfterMove)
                .isNotNull();
    }

    @Test
    public void testSendToUser() throws InterruptedException, ExecutionException, TimeoutException {

        StompSession stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/message/123", new AnnouncementFrameHandler());
        stompSession.send("/app/message/123", new OutputMessage("hi!"));
        OutputMessage gameStateAfterMove = completableFuture.get(5, SECONDS);

        assertThat(gameStateAfterMove)
                .isNotNull();
    }


    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class AnnouncementFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return OutputMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((OutputMessage) o);
        }
    }
}
