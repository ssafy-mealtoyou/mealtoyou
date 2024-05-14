package com.mealtoyou.alarmservice.application.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.alarmservice.infrastructure.kafka.KafkaMonoUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

	private final KafkaMonoUtils kafkaMonoUtils;
	private final ObjectMapper objectMapper;

	public Flux<String> noticeIntermittentStart() {
		return kafkaMonoUtils.sendAndReceive("user-service-getIntermittentStart",
			LocalTime.now().withSecond(0).withNano(0)).flatMapMany(result -> {
			try {
				List<String> fcmList = objectMapper.readValue(result, new TypeReference<>() {
				});
				return Flux.fromIterable(fcmList);
			} catch (Exception e) {
				// 변환 중 에러가 발생하면 빈 Flux 반환
				return Flux.empty();
			}
		});
	}

	public Flux<String> noticeIntermittentEnd() {
		return kafkaMonoUtils.sendAndReceive("user-service-getIntermittentEnd",
			LocalTime.now().withSecond(0).withNano(0)).flatMapMany(result -> {
			try {
				List<String> fcmList = objectMapper.readValue(result, new TypeReference<>() {
				});
				return Flux.fromIterable(fcmList);
			} catch (Exception e) {
				// 변환 중 에러가 발생하면 빈 Flux 반환
				return Flux.empty();
			}
		});
	}

	public Mono<Map<String, List<String>>> noticeSupplement() {
		return kafkaMonoUtils.sendAndReceive("supplement-service-getAlertTime",
				LocalTime.now().withSecond(0).withNano(0))
			.flatMap(result -> {
				try {
					Map<Long, List<String>> userIdToSupplementsMap = objectMapper.readValue(result,
						new TypeReference<>() {
						});

					List<Long> userIds = new ArrayList<>(userIdToSupplementsMap.keySet());

					return kafkaMonoUtils.sendAndReceive("user-service-getFcmToken", userIds)
						.flatMap(fcms -> {
							try {
								Map<Long, String> userIdToFcmTokenMap = objectMapper.readValue(fcms,
									new TypeReference<>() {
									});

								Map<String, List<String>> fcmTokenToSupplementsMap = new ConcurrentHashMap<>();
								userIdToSupplementsMap.forEach((userId, supplements) -> {
									String fcmToken = userIdToFcmTokenMap.get(userId);
									if (fcmToken != null) {
										fcmTokenToSupplementsMap.put(fcmToken, supplements);
									}
								});
								return Mono.just(fcmTokenToSupplementsMap);
							} catch (Exception e) {
								return Mono.empty();
							}
						});
				} catch (Exception e) {
					return Mono.empty();
				}
			});

	}
}