package com.mealtoyou.userservice.infrastructure.scheduler;

import java.time.LocalTime;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mealtoyou.userservice.application.service.FcmService;
import com.mealtoyou.userservice.domain.repository.IntermittentRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class IntermittentScheduler {
	private final FcmService fcmService;
	private final IntermittentRepository intermittentRepository;

	@Scheduled(cron = "0 * * * * *")
	private void noticeIntermittentStart() {
		intermittentRepository.findFcmTokenAndStartTime(LocalTime.now().withSecond(0).withNano(0))
			.collectList()
			.flatMapMany(tokens -> Flux.fromIterable(tokens)
				.flatMap(token -> fcmService.sendMessageByToken("MealToYou 단식알림", "단식 시작 시간입니다", token))
			)
			.then()
			.subscribe();
	}

	@Scheduled(cron = "0 * * * * *")
	private void noticeIntermittentEnd() {
		intermittentRepository.findFcmTokenAndEndTime(LocalTime.now().withSecond(0).withNano(0))
			.collectList()
			.flatMapMany(tokens -> Flux.fromIterable(tokens)
				.flatMap(token -> fcmService.sendMessageByToken("MealToYou 단식알림", "단식 종료시간입니다", token))
			)
			.then()
			.subscribe();
	}
}
