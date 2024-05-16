package com.mealtoyou.alarmservice.infrastructure.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mealtoyou.alarmservice.application.service.FcmService;
import com.mealtoyou.alarmservice.application.service.SchedulerService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class IntermittentScheduler {
	private final FcmService fcmService;
	private final SchedulerService schedulerService;

	@Scheduled(cron = "0 * * * * *")
	private void noticeIntermittentStart() {
		schedulerService.noticeIntermittentStart()
			.collectList()
			.flatMapMany(tokens -> Flux.fromIterable(tokens)
				.flatMap(token -> fcmService.sendMessageByToken("MealToYou 단식알림", "단식 시작 시간입니다", token)))
			.then()
			.subscribe();
	}

	@Scheduled(cron = "0 * * * * *")
	private void noticeIntermittentEnd() {
		schedulerService.noticeIntermittentEnd()
			.collectList()
			.flatMapMany(tokens -> Flux.fromIterable(tokens)
				.flatMap(token -> fcmService.sendMessageByToken("MealToYou 단식알림", "단식 종료 시간입니다", token)))
			.then().subscribe();
	}
}
