package com.mealtoyou.communityservice.application.service;

import com.mealtoyou.communityservice.application.dto.CreateCommunityDto;
import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.domain.repository.CommunityRepository;
import com.mealtoyou.communityservice.infrastructure.exception.CommunityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    public Mono<Community> createCommunity(CreateCommunityDto createCommunityDto, Long userId) {
        return communityRepository.findByLeaderId(userId)
                .flatMap(community -> Mono.<Community>error(new CommunityAlreadyExistsException("Community already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    Community community = Community.builder()
                            .leaderId(userId)
                            .title(createCommunityDto.title())
                            .cntUsers(1)
                            .startDate(LocalDateTime.now())
                            .endDate(LocalDateTime.now().plusDays(createCommunityDto.period()))
                            .dailyGoalCalories(createCommunityDto.dailyGoalCalories())
                            .dailyGoalSteps(createCommunityDto.dailyGoalSteps())
                            .weeklyMinGoal(createCommunityDto.weeklyMinGoal())
                            .build();
                    return communityRepository.save(community);
                }));
    }

}
