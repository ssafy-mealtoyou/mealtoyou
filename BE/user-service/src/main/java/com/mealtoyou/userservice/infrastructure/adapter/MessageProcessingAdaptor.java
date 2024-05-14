package com.mealtoyou.userservice.infrastructure.adapter;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.userservice.application.dto.response.ChattingUserInfoResponse;
import com.mealtoyou.userservice.application.service.UserService;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.IntermittentRepository;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMessageListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

	private final UserService userService;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;
	private final IntermittentRepository intermittentRepository;

	@KafkaMessageListener(topic = "getHeight")
	public Mono<Double> processMessage1(String message) {
		Long id = Long.parseLong(message);
		return userService.getHeight(id);
	}

	@KafkaMessageListener(topic = "getNickname")
	public Mono<String> getNickname(String message) {
		Long id = Long.parseLong(message);
		return userService.getNickname(id);
	}

	@KafkaMessageListener(topic = "getFcmToken")
	public Mono<Map<Long, String>> getFcmToken(String message) {
		try {
			List<Long> userIds = objectMapper.readValue(message, new TypeReference<>() {
			});
			return userRepository.findAllById(userIds).collectMap(User::getUserId, User::getFcmToken);

		} catch (JsonProcessingException e) {
			return Mono.empty();
		}
	}

	@KafkaMessageListener(topic = "getIntermittentStart")
	public Flux<String> getIntermittentStart(String message) {
		LocalTime start = LocalTime.parse(message.replace("\"", ""));
		return intermittentRepository.findFcmTokenAndStartTime(start);
	}

	@KafkaMessageListener(topic = "getIntermittentEnd")
	public Flux<String> getIntermittentEnd(String message) {
		LocalTime end = LocalTime.parse(message.replace("\"", ""));
		return intermittentRepository.findFcmTokenAndEndTime(end);
	}

	@KafkaMessageListener(topic = "userInfo")
	public Flux<ChattingUserInfoResponse> processMessage2(String message) {
		try {
			// 메시지를 List<Long>으로 변환
			List<Long> userIds = objectMapper.readValue(message, new TypeReference<>() {
			});
			// 한 번의 쿼리로 모든 사용자 정보 조회
			return userRepository.findAllById(userIds)
				.map(userInfo -> new ChattingUserInfoResponse(userInfo.getUserId(), userInfo.getNickname(),
					userInfo.getUserImageUrl()));
		} catch (Exception e) {
			// 변환 중 에러가 발생하면 빈 Flux 를 반환
			return Flux.empty();
		}
	}

	// @KafkaMessageListener(topic = "requests2")
	// public String processMessage2(String message) {
	//     return message + "2번 MSA입니다.";
	// }
	//
	// @KafkaMessageListener(topic = "getPerson")
	// public Flux<Person> personFlux(String message) {
	//     return personService.getAllPeople();
	// }
	//
	// @KafkaMessageListener(topic = "insertPerson")
	// public Mono<Person> personMono(String message) {
	//     return personService.createPerson(Person.builder().name("bono bono").build());
	// }
}
