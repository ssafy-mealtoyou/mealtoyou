package com.mealtoyou.communityservice.application.service;

import com.mealtoyou.communityservice.application.dto.CreateCommunityDto;
import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.domain.model.UserCommunity;
import com.mealtoyou.communityservice.domain.repository.CommunityRepository;
import com.mealtoyou.communityservice.domain.repository.UserCommunityRepository;
import com.mealtoyou.communityservice.infrastructure.exception.CommunityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserCommunityRepository userCommunityRepository;

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
                    return communityRepository.save(community)
                            .flatMap(savedCommunity -> {
                                UserCommunity userCommunity = UserCommunity.builder()
                                        .userId(userId)
                                        .communityId(savedCommunity.getCommunityId())
                                        .build();
                                log.info(savedCommunity.getCommunityId().toString());
                                return userCommunityRepository.save(userCommunity)
                                        .thenReturn(savedCommunity);
                            });
                }));
    }

}
