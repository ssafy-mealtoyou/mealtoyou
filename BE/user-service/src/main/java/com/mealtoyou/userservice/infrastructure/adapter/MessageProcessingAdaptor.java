package com.mealtoyou.userservice.infrastructure.adapter;

import org.springframework.stereotype.Service;

import com.mealtoyou.userservice.application.service.UserService;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMessageListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

    private final UserService userService;

    @KafkaMessageListener(topic = "getHeight")
    public Mono<Double> processMessage1(String message) {
        Long id = Long.parseLong(message);
        return userService.getHeight(id);
    }

    @KafkaMessageListener(topic = "getNickname")
    public Mono<String> getNickname(String message) {
        Long id = Long.parseLong(message);
        return userService.getNickname(id);
    }

    // @KafkaMessageListener(topic = "requests2")
    // public String processMessage2(String message) {
    //     return message + "2번 MSA입니다.";
    // }
    //
    // @KafkaMessageListener(topic = "getPerson")
    // public Flux<Person> personFlux(String message) {
    //     return personService.getAllPeople();
    // }
    //
    // @KafkaMessageListener(topic = "insertPerson")
    // public Mono<Person> personMono(String message) {
    //     return personService.createPerson(Person.builder().name("bono bono").build());
    // }
}
