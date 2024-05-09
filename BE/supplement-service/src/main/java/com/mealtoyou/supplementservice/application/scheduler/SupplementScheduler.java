package com.mealtoyou.supplementservice.application.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mealtoyou.supplementservice.application.service.SupplementService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SupplementScheduler {
	private final SupplementService supplementService;
	// 매일 자정에 실행
	// @Scheduled(cron = "0 0 0 * * ?")
	@Scheduled(cron = "0 * * * * ?")
	public void resetTakenYn() {
		System.out.println("hi");
		Mono<Void> resetMono = supplementService.resetTakenYn();

		// subscribe하여 Mono를 실행
		resetMono.subscribe();
	}
}
