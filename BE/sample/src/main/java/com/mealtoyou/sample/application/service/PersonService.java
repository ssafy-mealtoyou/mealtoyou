package com.mealtoyou.sample.application.service;

import com.mealtoyou.sample.domain.model.Person;
import com.mealtoyou.sample.domain.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Mono<Person> createPerson(Person person) {
        return personRepository.save(person);
    }

    public Flux<Person> getAllPeople() {
        return personRepository.findAll();
    }
}
