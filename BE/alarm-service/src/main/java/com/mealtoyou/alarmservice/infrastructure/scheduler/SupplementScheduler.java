package com.mealtoyou.alarmservice.infrastructure.scheduler;

import java.util.List;

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
public class SupplementScheduler {

	private final FcmService fcmService;
	private final SchedulerService schedulerService;

	@Scheduled(cron = "0 * * * * *")
	private void noticeIntermittentEnd() {
		schedulerService.noticeSupplement()
			.flatMapMany(tokenListMap -> Flux.fromIterable(tokenListMap.entrySet()))
			.flatMap(entry -> {
				String token = entry.getKey();
				List<String> supplements = entry.getValue();
				return Flux.fromIterable(supplements).flatMap(supplement -> {
					return fcmService.sendMessageByToken("MealToYou 영양제 알림", supplement + " 복용시간입니다", token);
				});
			})
			.then()
			.subscribe();
	}
}
