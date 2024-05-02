package com.mealtoyou.healthservice.application.dto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.Temporal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// @JsonDeserialize(using = ComparableDeserializer.class)
public class ExerciseRequestDto {
	private Long steps;
	private LocalDate stepStartDate;
	// private Temporal stepEndDate;
	private Double caloriesBurned;
	private LocalDate caloriesStartDate;
	// private Temporal caloriesEndDate;
}
