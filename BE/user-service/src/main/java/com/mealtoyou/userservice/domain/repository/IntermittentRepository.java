package com.mealtoyou.userservice.domain.repository;

import java.time.LocalTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.mealtoyou.userservice.domain.model.Intermittent;

import io.lettuce.core.dynamic.annotation.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IntermittentRepository extends ReactiveCrudRepository<Intermittent, Long> {
	Mono<Intermittent> findByUserId(Long userId);

	@Query("select users.fcm_token\n"
		+ "from intermittent_fasting_timer as intermittent\n"
		+ "         left join users on intermittent.user_id = users.user_id\n"
		+ "where intermittent.start_time = :startTime\n"
		+ "  and users.fcm_token is not null;")
	Flux<String> findFcmTokenAndStartTime(@Param("startTime") LocalTime startTime);

	@Query("select users.fcm_token\n"
		+ "from intermittent_fasting_timer as intermittent\n"
		+ "         left join users on intermittent.user_id = users.user_id\n"
		+ "where intermittent.end_time = :endTime\n"
		+ "  and users.fcm_token is not null;")
	Flux<String> findFcmTokenAndEndTime(@Param("endTime") LocalTime endTime);
}
