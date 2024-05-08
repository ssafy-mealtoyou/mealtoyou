package com.mealtoyou.communityservice.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.communityservice.application.dto.RecentMessage;
import com.mealtoyou.communityservice.application.dto.UserHealthInfo;
import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.domain.model.UserCommunity;
import com.mealtoyou.communityservice.domain.repository.CommunityDietRepository;
import com.mealtoyou.communityservice.domain.repository.CommunityRepository;
import com.mealtoyou.communityservice.domain.repository.UserCommunityRepository;
import com.mealtoyou.communityservice.infrastructure.exception.CommunityAlreadyExistsException;
import com.mealtoyou.communityservice.infrastructure.exception.EmptyCommunityException;
import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMonoUtils;
import com.mealtoyou.communityservice.presentation.request.CreateCommunityRequest;
import com.mealtoyou.communityservice.presentation.request.UpdateCommunityRequest;
import com.mealtoyou.communityservice.presentation.response.CommunityDietResponse;
import com.mealtoyou.communityservice.presentation.response.CommunityResponse;
import com.mealtoyou.communityservice.presentation.response.UserCommunityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserCommunityRepository userCommunityRepository;
    private final CommunityDietRepository communityDietRepository;
    private final KafkaMonoUtils kafkaMonoUtils;
    private final ObjectMapper objectMapper;
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

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
        return userCommunityRepository.findById(userId)
                .flatMap(userCommunity -> {
                    // user_community 에서 community 아이디 조회
                    Long communityId = userCommunity.getCommunityId();
                    // community 조회
                    return communityRepository.findById(communityId)
                            .flatMap(community -> {
                                // 최근 채팅 내용 조회
                                Mono<List<String>> recentChatContents = getRecentChatContents(communityId);
                                // 공유 식단 리스트 조회
                                Mono<List<CommunityDietResponse>> sharedMenuList = getSharedMenuList(communityId);
                                // 주당 일일 목표 달성 횟수 조회
                                Mono<Integer> weeklyRemainGoal = calculateWeeklyRemainGoal(userId);
                                // 걸음 수 및 소모 칼로리 조회
                                Mono<UserHealthInfo> userHealthInfo = fetchAndConvertUserHealthInfo(userId);
                                // Mono 의 zip 연산자를 사용하여 모든 결과를 조합하여 UserCommunityResponse 를 생성
                                return Mono.zip(recentChatContents, sharedMenuList, weeklyRemainGoal, userHealthInfo)
                                        .map(tuple -> UserCommunityResponse.toEntity(
                                                community,
                                                tuple.getT1(),
                                                tuple.getT2(),
                                                tuple.getT3(),
                                                tuple.getT4().steps(),
                                                tuple.getT4().caloriesBurned()
                                        ));
                            });
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

    public Mono<String> dailyGoalCheck(Long userId, int steps, int caloriesBurned) {
        return getUserCommunity(userId)
                .flatMap(community -> {
                    // 목표를 달성했는지 확인
                    if (isGoalAchieved(steps, caloriesBurned, community)) {
                        return Mono.just("fail"); // 목표 달성 실패
                    }
                    // Redis 에 목표 체크를 업데이트
                    return updateDailyGoalCheck(userId);
                });
    }

    // 목표를 달생했는지 확인
    private boolean isGoalAchieved(int steps, int caloriesBurned, Community community) {
        Integer dailyGoalSteps = community.getDailyGoalSteps();
        Integer dailyGoalCalories = community.getDailyGoalCalories();

        // 걸음 수와 소모 칼로리가 목표치를 달성했는지 확인
        return steps >= dailyGoalSteps && caloriesBurned >= dailyGoalCalories;
    }

    private Mono<List<String>> getRecentChatContents(Long communityId) {
        return kafkaMonoUtils.sendAndReceive("chatting-service-recentChat", communityId)
                .flatMap(result -> {
                    RecentMessage recentMessage = objectMapper.convertValue(result, RecentMessage.class);
                    List<String> messages = List.of(recentMessage.message1(), recentMessage.message2(), recentMessage.message3());
                    return Mono.just(messages);
                });
    }

    private Mono<List<CommunityDietResponse>> getSharedMenuList(Long communityId) {
        return communityDietRepository.findByCommunityId(communityId)
                .collectList()
                .flatMap(dietIds -> {
                    // Kafka 에 dietIds 를 보내고 결과를 받아옴
                    return kafkaMonoUtils.sendAndReceive("diet-service-communityDiet", dietIds)
                            .flatMap(result -> {
                                // 결과를 CommunityDietResponse 객체의 리스트로 변환
                                List<CommunityDietResponse> responses = objectMapper.convertValue(result, new TypeReference<>() {
                                });
                                return Mono.just(responses);
                            });
                });
    }

    private Mono<Integer> calculateWeeklyRemainGoal(Long userId) {
        return reactiveRedisTemplate.opsForValue().get(userId)
                .map(value -> Integer.bitCount(Integer.parseInt(value))) // 조회된 값을 Integer 로 변환 후 이진수에서 1의 개수 계산
                .switchIfEmpty(Mono.just(0)); // 값이 없을 경우 0 리턴
    }

    // Kafka 를 통해 건강 정보를 받아와 UserHealthInfo 객체로 변환하는 메소드
    private Mono<UserHealthInfo> fetchAndConvertUserHealthInfo(Long userId) {
        return kafkaMonoUtils.sendAndReceive("health-service-healthInfo", userId)
                .map(result -> objectMapper.convertValue(result, UserHealthInfo.class));
    }

    // 사용자의 커뮤니티 정보를 가져오는 Mono 생성
    private Mono<Community> getUserCommunity(Long userId) {
        return userCommunityRepository.findByUserId(userId)
                .flatMap(userCommunity -> communityRepository.findById(userCommunity.getCommunityId()));
    }

    // Redis 에 목표 체크를 업데이트
    private Mono<String> updateDailyGoalCheck(Long userId) {
        ReactiveValueOperations<String, String> ops = reactiveRedisTemplate.opsForValue();

        return ops.get(String.valueOf(userId))
                .flatMap(redisValue -> {
                    LocalDateTime now = LocalDateTime.now();
                    int dayValue = now.getDayOfWeek().getValue();

                    if (redisValue != null) {
                        char c = redisValue.charAt(7 - dayValue);
                        // 해당 요일의 목표가 이미 달성되었으면 Redis 작업
                        if (c == '0') {
                            int dailyGoalCheck = Integer.parseInt(redisValue, 2);
                            int newDailyGoalCheck = dailyGoalCheck | (1 << (7 - dayValue));
                            // Redis 에 새로운 값 저장
                            return ops.set(userId.toString(), String.valueOf(newDailyGoalCheck))
                                    .map(saved -> "success");
                        }
                        // 이미 목표가 달성된 경우
                        return Mono.just("success");
                    } else {
                        // 만료시간 계산
                        LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY);
                        Duration durationSinceStartOfWeek = Duration.between(startOfWeek, now);
                        long secondsUntilEndOfWeek = Duration.ofDays(7).minus(durationSinceStartOfWeek).getSeconds();
                        int newDailyGoalCheck = 1 << (7 - dayValue);
                        // Redis 에 새로운 값 등록
                        return ops.setIfAbsent(String.valueOf(userId), String.valueOf(newDailyGoalCheck), Duration.ofSeconds(secondsUntilEndOfWeek))
                                .map(saved -> "success");
                    }
                });
    }
}
