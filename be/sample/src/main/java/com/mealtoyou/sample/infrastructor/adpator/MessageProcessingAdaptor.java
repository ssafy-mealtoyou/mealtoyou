package com.mealtoyou.sample.infrastructor.adpator;


import com.mealtoyou.sample.application.service.PersonService;
import com.mealtoyou.sample.domain.model.Person;
import com.mealtoyou.sample.infrastructor.kafka.KafkaMessageEnable;
import com.mealtoyou.sample.infrastructor.kafka.KafkaMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {
    private final PersonService personService;
    @KafkaMessageListener(topic = "requests1")
    public String processMessage1(String message) {
        return message + "1번 MSA입니다.";
    }

    @KafkaMessageListener(topic = "requests2")
    public String processMessage2(String message) {
        return message + "2번 MSA입니다.";
    }

    @KafkaMessageListener(topic = "getPerson")
    public Flux<Person> personFlux(String message) {
        return personService.getAllPeople();
    }

    @KafkaMessageListener(topic = "insertPerson")
    public Mono<Person> personMono(String message) {
        return personService.createPerson(Person.builder().name("bono bono").build());
    }
}
