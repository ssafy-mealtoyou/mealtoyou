package com.mealtoyou.communityservice.infrastructure.adpator;


import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

    @KafkaMessageListener(topic = "requests1")
    public String processMessage1(String message) {
        return message + "1번 MSA입니다.";
    }

    @KafkaMessageListener(topic = "requests2")
    public String processMessage2(String message) {
        return message + "2번 MSA입니다.";
    }

}
