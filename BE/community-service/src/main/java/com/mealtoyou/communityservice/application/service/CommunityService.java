package com.mealtoyou.communityservice.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.communityservice.application.dto.CommunityDietsRequestDto;
import com.mealtoyou.communityservice.application.dto.RecentMessage;
import com.mealtoyou.communityservice.application.dto.UserHealthInfo;
import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.domain.model.CommunityDiet;
import com.mealtoyou.communityservice.domain.model.UserCommunity;
import com.mealtoyou.communityservice.domain.repository.CommunityDietRepository;
import com.mealtoyou.communityservice.domain.repository.CommunityRepository;
import com.mealtoyou.communityservice.domain.repository.UserCommunityRepository;
import com.mealtoyou.communityservice.infrastructure.exception.CommunityAlreadyExistsException;
import com.mealtoyou.communityservice.infrastructure.exception.EmptyCommunityException;
import com.mealtoyou.communityservice.infrastructure.kafka.KafkaMonoUtils;
import com.mealtoyou.communityservice.presentation.request.CreateCommunityRequest;
import com.mealtoyou.communityservice.presentation.request.UpdateCommunityRequest;
import com.mealtoyou.communityservice.presentation.response.ChattingUserInfoResponse;
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
import java.util.ArrayList;
import java.util.Collections;
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
        return userCommunityRepository.findByUserId(userId)
                .flatMap(userCommunity -> {
                    // user_community 에서 community 아이디 조회
                    Long communityId = userCommunity.getCommunityId();
                    // community 조회
                    return communityRepository.findById(communityId)
                            .flatMap(community -> {
                                // 최근 채팅 내용 조회
//                                Mono<List<String>> recentChatContents = getRecentChatContents(communityId);
                                // 공유 식단 리스트 조회
                                Mono<List<CommunityDietResponse>> sharedMenuList = getSharedMenuList(communityId, userId);
                                // 주당 일일 목표 달성 횟수 조회
                                Mono<Integer> weeklyRemainGoal = calculateWeeklyRemainGoal(userId);
                                // 걸음 수 및 소모 칼로리 조회
                                Mono<UserHealthInfo> userHealthInfo = fetchAndConvertUserHealthInfo(userId);
                                // Mono 의 zip 연산자를 사용하여 모든 결과를 조합하여 UserCommunityResponse 를 생성
                                return Mono.zip(sharedMenuList, weeklyRemainGoal, userHealthInfo)
                                        .map(tuple -> UserCommunityResponse.toEntity(
                                                community,
                                                tuple.getT1(),
                                                tuple.getT2(),
                                                tuple.getT3()
                                        ));
                            });
                });
    }

    // 기본 값을 반환하는 메서드 정의
    private Mono<List<CommunityDietResponse>> getDefaultSharedMenuList() {
        // 기본 값을 반환하도록 정의
        // 예를 들어, 빈 리스트를 반환하거나 기타 기본 값을 설정할 수 있음
        return Mono.just(new ArrayList<>()); // 빈 리스트 반환
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
                    if (!isGoalAchieved(steps, caloriesBurned, community)) {
                        return Mono.just("fail"); // 목표 달성 실패
                    }
                    // Redis 에 목표 체크를 업데이트
                    return updateDailyGoalCheck(userId);
                });
    }

    public Flux<ChattingUserInfoResponse> getChattingUserInfo(Long communityId) {
        return userCommunityRepository.findByCommunityId(communityId)
                .map(UserCommunity::getUserId) // UserCommunity 에서 userId 추출
                .collectList() // 추출된 userId 들을 List 로 변환
                .flatMapMany(userIds ->
                        kafkaMonoUtils.sendAndReceive("user-service-userInfo", userIds)
                                .flatMapMany(result -> {
                                    try {
                                        List<ChattingUserInfoResponse> userInfoList
                                                = objectMapper.readValue(result, new TypeReference<>() {
                                        });
                                        // 변환된 List 를 Flux 로 변환
                                        return Flux.fromIterable(userInfoList);
                                    } catch (Exception e) {
                                        // 변환 중 에러가 발생하면 빈 Flux 반환
                                        return Flux.empty();
                                    }
                                })
                );
    }

    public Flux<ChattingUserInfoResponse> getChattingUserInfoOne(Long userId) {
        return kafkaMonoUtils.sendAndReceive("user-service-userInfo", List.of(userId))
                .flatMapMany(result -> {
                    try {
                        List<ChattingUserInfoResponse> userInfoList
                                = objectMapper.readValue(result, new TypeReference<>() {
                        });
                        // 변환된 List 를 Flux 로 변환
                        return Flux.fromIterable(userInfoList);
                    } catch (Exception e) {
                        // 변환 중 에러가 발생하면 빈 Flux 반환
                        return Flux.empty();
                    }
                });
    }

    public Mono<String> registerCommunity(Long userId, Long communityId) {
        return userCommunityRepository.findByUserId(userId)
                .flatMap(userCommunity -> {
                    // 이미 커뮤니티에 가입된 경우
                    return Mono.just("already joined community");
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // 사용자가 아직 커뮤니티에 가입하지 않은 경우 새로운 UserCommunity 객체 생성 및 저장
                    UserCommunity newUserCommunity = UserCommunity.builder()
                            .userId(userId)
                            .communityId(communityId)
                            .build();
                    return userCommunityRepository.save(newUserCommunity)
                            .flatMap(result -> Mono.just("success"));
                }));
    }

    // 목표를 달생했는지 확인
    private boolean isGoalAchieved(int steps, int caloriesBurned, Community community) {
        Integer dailyGoalSteps = community.getDailyGoalSteps();
        Integer dailyGoalCalories = community.getDailyGoalCalories();

        // 걸음 수와 소모 칼로리가 목표치를 달성했는지 확인
        return steps >= dailyGoalSteps && caloriesBurned >= dailyGoalCalories;
    }

//    private Mono<List<String>> getRecentChatContents(Long communityId) {
//        return kafkaMonoUtils.sendAndReceive("chatting-service-recentChat", communityId)
//                .flatMap(result -> {
//                    RecentMessage recentMessage = objectMapper.convertValue(result, RecentMessage.class);
//                    List<String> messages = List.of(recentMessage.message1(), recentMessage.message2(), recentMessage.message3());
//                    log.info("messages: {}", messages.get(0));
//                    return Mono.just(messages);
//                });
//    }

    private Mono<List<CommunityDietResponse>> getSharedMenuList(Long communityId, Long userId) {
        return communityDietRepository.findByCommunityId(communityId)
                .map(CommunityDiet::getDietId) // CommunityDiet에서 dietId만 추출
                .collectList()
                .flatMap(dietIds -> {
                    CommunityDietsRequestDto communityDietsRequestDto = CommunityDietsRequestDto.builder()
                            .userId(userId)
                            .dietIdList(dietIds)
                            .build();
                    // Kafka 에 dietIds 를 보내고 결과를 받아옴
                    return kafkaMonoUtils.sendAndReceive("food-service-community-diet-list", communityDietsRequestDto)
                            .flatMap(result -> {
                                // result 가 JSON 형식이 아닌 문자열인지 확인
                                if (!result.startsWith("[")) {
                                    // JSON 형식이 아닌 경우 처리
                                    log.error("Error: Not a JSON string");
                                    // 기본 값으로 빈 리스트 대신 원하는 값을 반환하도록 수정
                                    return getDefaultSharedMenuList(); // 기본 값을 반환하는 메서드 호출
                                }
                                try {
                                    // JSON 형식인지 확인하고, JSON을 CommunityDietResponse 객체의 리스트로 변환
                                    List<CommunityDietResponse> responses = objectMapper.readValue(result, new TypeReference<>() {
                                    });
                                    log.info("CommunityDietResponse: {}", responses.get(0));
                                    return Mono.just(responses);
                                } catch (JsonProcessingException e) {
                                    // JSON 형식이 아닌 경우 처리
                                    log.error("Error parsing JSON: {}", e.getMessage());
                                    // 기본 값으로 빈 리스트 대신 원하는 값을 반환하도록 수정
                                    return getDefaultSharedMenuList(); // 기본 값을 반환하는 메서드 호출
                                }
                            });
                });
    }


    private Mono<Integer> calculateWeeklyRemainGoal(Long userId) {
        return reactiveRedisTemplate.opsForValue().get(userId.toString())
                .flatMap(value -> {
                    if (value != null) {
                        return Mono.just(Integer.bitCount(Integer.parseInt(value))); // 조회된 값을 Integer로 변환 후 이진수에서 1의 개수 계산
                    } else {
                        return Mono.just(0); // 값이 없을 경우 0 반환
                    }
                })
                .switchIfEmpty(Mono.just(0)); // 값이 없을 경우 0 반환
    }


    // Kafka 를 통해 건강 정보를 받아와 UserHealthInfo 객체로 변환하는 메소드
    private Mono<UserHealthInfo> fetchAndConvertUserHealthInfo(Long userId) {
        return kafkaMonoUtils.sendAndReceive("health-service-healthInfo", userId)
                .<UserHealthInfo>handle((result, sink) -> {
                    try {
                        sink.next(objectMapper.readValue(result, UserHealthInfo.class));
                    } catch (JsonProcessingException e) {
                        sink.error(new RuntimeException(e));
                    }
                })
                .switchIfEmpty(Mono.just(new UserHealthInfo(0, 0)));
    }

    // 사용자의 커뮤니티 정보를 가져오는 Mono 생성
    private Mono<Community> getUserCommunity(Long userId) {
        return userCommunityRepository.findByUserId(userId)
                .flatMap(userCommunity -> communityRepository.findById(userCommunity.getCommunityId()));
    }

    // Redis 에 목표 체크를 업데이트
    private Mono<String> updateDailyGoalCheck(Long userId) {
        ReactiveValueOperations<String, String> ops = reactiveRedisTemplate.opsForValue();
        LocalDateTime now = LocalDateTime.now();
        int dayValue = now.getDayOfWeek().getValue();
        String key = String.valueOf(userId);
        Mono<String> redisValueMono = ops.get(key)
                .switchIfEmpty(Mono.just("")); // 값이 없으면 빈 Mono 반환

        return redisValueMono.flatMap(redisValue -> {
            // Redis 에 저장된 값이 있는 경우
            if (!redisValue.isEmpty()) {
                int dailyGoalCheck = Integer.parseInt(redisValue, 2);

                // 해당 요일의 목표가 이미 달성되었으면 Redis 작업
                if ((dailyGoalCheck & (1 << (6 - dayValue))) != 0) {
                    return Mono.just("success"); // 이미 목표가 달성된 경우
                }

                // 해당 요일의 목표를 달성한 것으로 표시
                int newDailyGoalCheck = dailyGoalCheck | (1 << (6 - dayValue));
                return ops.set(key, Integer.toBinaryString(newDailyGoalCheck))
                        .map(saved -> "success");
            } else {
                // 만료시간 계산
                LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY);
                Duration durationSinceStartOfWeek = Duration.between(startOfWeek, now);
                long secondsUntilEndOfWeek = Duration.ofDays(7).minus(durationSinceStartOfWeek).getSeconds();

                // 새로운 목표 설정
                int newDailyGoalCheck = 1 << (6 - dayValue);
                return ops.setIfAbsent(key, Integer.toBinaryString(newDailyGoalCheck), Duration.ofSeconds(secondsUntilEndOfWeek))
                        .map(saved -> "success");
            }
        }).doOnError(e -> {
            log.error("Redis error: {}", e.getMessage()); // Redis에서 에러가 발생한 경우 로그 출력
        });
    }

    public Mono<String> checkStatus(Long userId) {
        return userCommunityRepository.findByUserId(userId)
                .flatMap(userCommunity -> Mono.just("true"))
                .switchIfEmpty(Mono.just("false"));
    }
}
