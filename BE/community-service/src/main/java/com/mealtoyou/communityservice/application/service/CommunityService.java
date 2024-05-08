package com.mealtoyou.communityservice.application.service;

import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.domain.model.UserCommunity;
import com.mealtoyou.communityservice.domain.repository.CommunityRepository;
import com.mealtoyou.communityservice.domain.repository.UserCommunityRepository;
import com.mealtoyou.communityservice.infrastructure.exception.CommunityAlreadyExistsException;
import com.mealtoyou.communityservice.infrastructure.exception.EmptyCommunityException;
import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMonoUtils;
import com.mealtoyou.communityservice.presentation.request.CreateCommunityRequest;
import com.mealtoyou.communityservice.presentation.request.UpdateCommunityRequest;
import com.mealtoyou.communityservice.presentation.response.CommunityDiet;
import com.mealtoyou.communityservice.presentation.response.CommunityResponse;
import com.mealtoyou.communityservice.presentation.response.UserCommunityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserCommunityRepository userCommunityRepository;
    private final KafkaMonoUtils kafkaMonoUtils;

    public Mono<Community> createCommunity(CreateCommunityRequest createCommunityRequest, Long userId) {
        return communityRepository.findByLeaderId(userId)
                .flatMap(community -> Mono.<Community>error(new CommunityAlreadyExistsException("Community already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    Community community = Community.builder()
                            .leaderId(userId)
                            .title(createCommunityRequest.title())
                            .cntUsers(1)
                            .startDate(LocalDateTime.now())
                            .endDate(LocalDateTime.now().plusDays(createCommunityRequest.period()))
                            .dailyGoalCalories(createCommunityRequest.dailyGoalCalories())
                            .dailyGoalSteps(createCommunityRequest.dailyGoalSteps())
                            .weeklyMinGoal(createCommunityRequest.weeklyMinGoal())
                            .build();
                    return communityRepository.save(community);
                }))
                .flatMap(savedCommunity -> {
                    UserCommunity userCommunity = UserCommunity.builder()
                            .userId(userId)
                            .communityId(savedCommunity.getCommunityId())
                            .build();
                    return userCommunityRepository.save(userCommunity)
                            .thenReturn(savedCommunity);
                });
    }

    public Mono<UserCommunityResponse> getUserCommunityInfo(Long userId) {
        // user_community 아이디로 조회
        return userCommunityRepository.findById(userId)
                .flatMap(userCommunity -> {
                    // community 조회
                    return communityRepository.findById(userCommunity.getCommunityId());
                }).map(community -> {
                    // todo: 최근 채팅 내용 3개
                    List<String> recentChatContents = getRecentChatContents(community.getCommunityId());
                    // todo: 공유 식단 리스트
                    List<CommunityDiet> sharedMenuList = getSharedMenuList(community.getCommunityId());
                    // todo: 남은 주당 일일 목표 달성 횟수 조회(redis 에서 조회 ex1001000)
                    Integer weeklyRemainGoal = calculateWeeklyRemainGoal(community);
                    // todo: 걸음 수 및 소모 칼로리 조회
                    Integer steps = 5000;
                    Integer caloriesBurned = 300;

                    return UserCommunityResponse
                            .toEntity(
                                    community,
                                    recentChatContents,
                                    sharedMenuList,
                                    weeklyRemainGoal,
                                    steps,
                                    caloriesBurned
                            );
                });
    }

    public Mono<Community> updateCommunity(UpdateCommunityRequest updateCommunityRequest, Long userId) {
        return communityRepository.findByLeaderId(userId)
                .flatMap(findCommunity -> {
                    if (findCommunity == null) {
                        return Mono.error(new EmptyCommunityException("Community is not exist"));
                    }
                    Community updateCommunity = UpdateCommunityRequest.toEntity(findCommunity, updateCommunityRequest);
                    return communityRepository.save(updateCommunity);
                });
    }

    public Flux<CommunityResponse> getCommunityList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return communityRepository.findAllByOrderByCommunityIdAsc(pageRequest)
                .map(CommunityResponse::toEntity);
    }


    private List<String> getRecentChatContents(Long communityId) {

        return List.of("Chat 1", "Chat 2", "Chat 3");
    }

    private List<CommunityDiet> getSharedMenuList(Long communityId) {
        return List.of(
                new CommunityDiet("Menu 1", 20, 40, 20, new ArrayList<>()),
                new CommunityDiet("Menu 2", 30, 30, 20, new ArrayList<>())
        );
    }

    private Integer calculateWeeklyRemainGoal(Community community) {
        return 2;
    }

}
