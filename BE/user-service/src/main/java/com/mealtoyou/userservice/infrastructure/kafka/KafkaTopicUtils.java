package com.mealtoyou.userservice.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicUtils {
    @Value("${spring.application.name}")
    private String appName;

    public String getRequestTopic(String topic) {
        return appName + "-" + topic;
    }

    public String getResponseTopic() {
        return appName + "-responses";
    }
}
