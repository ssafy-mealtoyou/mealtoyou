package com.mealtoyou.userservice.presentation.controller;

import com.mealtoyou.userservice.application.service.JwtTokenProvider;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/users/profile")
    public Mono<User> getUserProfile(@RequestHeader("Authorization") String token) {
        return userRepository.findById(jwtTokenProvider.getUserId(token));
    }

    @PostMapping("/users/profile")
    public Mono<String> updateUserProfile(@RequestHeader("Authorization") String token) {
        return null;
    }

}
