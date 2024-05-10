package com.mealtoyou.userservice.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mealtoyou.userservice.application.dto.request.UserGoalRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInbodyRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserWeightRequestDto;
import com.mealtoyou.userservice.application.dto.response.UserInfoResponseDto;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMonoUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final S3Uploader s3Uploader;
	private final KafkaMonoUtils kafkaMonoUtils;

	private Mono<Boolean> requestSavingUserHealth(UserInbodyRequestDto requestDto) {
		return kafkaMonoUtils.sendAndReceive("health-service-save-user-inbody", requestDto).map(Boolean::parseBoolean).onErrorResume((e) -> Mono.just(false));
	}

	public Mono<UserInfoResponseDto> getUserProfile(long userId) {
		return userRepository.findById(userId).flatMap(user -> {
			if (user == null) {
				return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
			}
			return Mono.just(UserInfoResponseDto.fromEntity(user));
		});
	}

	public Mono<UserInfoResponseDto> updateUserProfile(long userId, FilePart image,
			UserInfoRequestDto userInfoRequestDto) {
		Mono<String> imageUrl;
		if(image!=null){
			imageUrl = s3Uploader.upload(image);
		} else {
			imageUrl = userRepository.findById(userId).map(User::getUserImageUrl);
    }
    return imageUrl.flatMap(url ->
				userRepository.findById(userId)
						.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
						.flatMap(user -> {
							user.updateUserInfo(userInfoRequestDto, url);
							return userRepository.save(user);
						})
						.map(UserInfoResponseDto::fromEntity));
	}

	public Mono<Double> getHeight(Long userId) {
		return userRepository
			.findById(userId).map(User::getHeight);
	}

	public Mono<String> getNickname(Long userId) {
		return userRepository
			.findById(userId).map(User::getNickname);
	}

	public Mono<Void> updateGoal(long userId, UserGoalRequestDto requestDto) {
		return userRepository.findById(userId)
			.flatMap(user -> {
				user.updateGoal(requestDto);
				return userRepository.save(user).then();
			});
	}

	public Mono<Void> updateWeight(long userId, UserWeightRequestDto requestDto) {
		return userRepository.findById(userId).flatMap(user -> {
			user.updateWeight(requestDto.weight());
			return userRepository.save(user);
		}).then();
	}

	public Mono<Void> updateInbody(long userId, String token, UserInbodyRequestDto requestDto) {
		return userRepository.findById(userId).flatMap(user -> {
			if (user.getHeight() <= 0.0 || user.getWeight() <= 0.0) {
				return Mono.error(new RuntimeException("유효하지 않은 "));
			}
			requestDto.setOthers(token, user.getWeight(), user.getHeight(), user.getAge());
			return requestSavingUserHealth(requestDto);
		})
		.flatMap(res -> {
			if (!res)
				return Mono.error(new RuntimeException());
			return Mono.empty();
		});
	}
}
