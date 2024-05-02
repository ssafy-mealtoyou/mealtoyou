package com.mealtoyou.communityservice.presentation.controller;

import com.mealtoyou.communityservice.application.dto.CreateCommunityDto;
import com.mealtoyou.communityservice.application.service.CommunityService;
import com.mealtoyou.communityservice.application.service.JwtTokenProvider;
import com.mealtoyou.communityservice.domain.model.Community;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CommunityController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CommunityService communityService;

    @PostMapping("/communities")
    public Mono<Community> createCommunity(@RequestHeader("Authorization") String token,
                                           @Valid @RequestBody CreateCommunityDto createCommunityDto) {
        Long userId = jwtTokenProvider.getUserId(token);
        return communityService.createCommunity(createCommunityDto, userId);
    }

}
