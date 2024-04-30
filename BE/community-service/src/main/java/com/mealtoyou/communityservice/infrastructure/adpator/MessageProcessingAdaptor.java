package com.mealtoyou.communityservice.infrastructure.adpator;


import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

//    @KafkaMessageListener(topic = "getPerson")
//    public Flux<Person> personFlux(String message) {
//        return personService.getAllPeople();
//    }
//
//    @KafkaMessageListener(topic = "insertPerson")
//    public Mono<Person> personMono(String message) {
//        return personService.createPerson(Person.builder().name("bono bono").build());
//    }
}
