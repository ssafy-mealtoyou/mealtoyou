package com.mealtoyou.sample.presentation.controller;


import com.mealtoyou.sample.application.service.PersonService;
import com.mealtoyou.sample.domain.model.Person;
import com.mealtoyou.sample.infrastructor.kafka.KafkaMonoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final KafkaMonoUtils kafkaMonoUtils;

    private final PersonService personService;

    @GetMapping("/send")
    public Mono<ResponseEntity<String>> sendRequest1(String message) {
        return kafkaMonoUtils.sendAndReceive("sample-requests1", message)
                .map(ResponseEntity::ok)
                .timeout(Duration.ofSeconds(30), Mono.just(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Response timed out")));
    }

    @GetMapping("/send2")
    public Mono<ResponseEntity<String>> sendRequest2(String message) {
        return processRequestChain(message)
                .map(ResponseEntity::ok)
                .timeout(Duration.ofSeconds(30), Mono.just(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Timeout occurred")));
    }

    private Mono<String> processRequestChain(String initialMessage) {
        return kafkaMonoUtils.sendAndReceive("sample-requests1", initialMessage)
                .flatMap(firstResult -> kafkaMonoUtils.sendAndReceive("sample-requests2", firstResult));
    }

    @GetMapping("/people")
    public Mono<String> getAllPeople() {
        return kafkaMonoUtils.sendAndReceive("sample-getPerson", "");
    }

    @GetMapping("/people2")
    public Flux<Person> getAllPeople2() {
        return personService.getAllPeople();
    }

    @GetMapping("/insert")
    public Mono<String> insert() {
        return kafkaMonoUtils.sendAndReceive("sample-insertPerson", "");
    }
}
