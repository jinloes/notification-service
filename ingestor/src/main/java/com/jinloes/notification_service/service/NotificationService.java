package com.jinloes.notification_service.service;

import com.jinloes.notification_service.Notification;
import com.jinloes.notification_service.NotificationResponse;
import com.jinloes.notification_service.NotificationServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class NotificationService extends NotificationServiceGrpc.NotificationServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Override
    public void send(Notification request, StreamObserver<NotificationResponse> responseObserver) {
        LOGGER.info("Received request: {}", request);

        NotificationResponse response = NotificationResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
