package com.mealtoyou.userservice.domain.repository;

import com.mealtoyou.userservice.domain.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByEmail(String email);
    @NonNull
    Flux<User> findAllById(@NonNull Iterable<Long> userIds);


}
