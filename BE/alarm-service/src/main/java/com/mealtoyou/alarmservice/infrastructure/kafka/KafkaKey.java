package com.mealtoyou.alarmservice.infrastructure.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaKey {
	private String uuid;
	private String kafkaResponseTopic;

	@JsonCreator
	public KafkaKey(@JsonProperty("uuid") String uuid,
		@JsonProperty("kafkaResponseTopic") String kafkaResponseTopic) {
		this.uuid = uuid;
		this.kafkaResponseTopic = kafkaResponseTopic;
	}
}
