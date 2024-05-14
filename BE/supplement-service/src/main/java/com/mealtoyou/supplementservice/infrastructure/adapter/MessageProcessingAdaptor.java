package com.mealtoyou.supplementservice.infrastructure.adapter;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mealtoyou.supplementservice.domain.model.Supplement;
import com.mealtoyou.supplementservice.domain.repository.SupplementRepository;
import com.mealtoyou.supplementservice.infrastructure.kafka.KafkaMessageEnable;
import com.mealtoyou.supplementservice.infrastructure.kafka.KafkaMessageListener;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@KafkaMessageEnable
@RequiredArgsConstructor
public class MessageProcessingAdaptor {

	private final SupplementRepository supplementRepository;

	@KafkaMessageListener(topic = "getAlertTime")
	public Mono<Map<Long, List<String>>> getAlertTime(String message) {
		LocalTime alert = LocalTime.parse(message.replace("\"", ""));
		return supplementRepository.findAllByAlertTime(alert).collectList().map(list -> list.stream()
			.collect(Collectors.groupingBy(Supplement::getUserId,
				Collectors.mapping(Supplement::getName, Collectors.toList()))));
	}
}
