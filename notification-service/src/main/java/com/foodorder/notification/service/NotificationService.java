package com.foodorder.notification.service;

import com.foodorder.notification.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public Map<String, Object> dispatch(NotificationRequest request) {
        log.info("Dispatching notification | userId={} | channel={} | subject={}", request.userId(), request.channel(), request.subject());
        return Map.of(
                "status", "SENT",
                "timestamp", Instant.now().toString(),
                "userId", request.userId(),
                "channel", request.channel(),
                "subject", request.subject()
        );
    }
}
