package com.mealtoyou.foodservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mealtoyou.foodservice.domain.model.Food;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FoodDto {
	private Long id;
	private String name;
	@JsonProperty("serving_unit")
	private String servingUnit;
	@JsonProperty("serving_size")
	private Double servingSize;
	@JsonProperty("protein(g)")
	private Double protein;
	@JsonProperty("energy(kcal)")
	private Double energy;
	@JsonProperty("fat(g)")
	private Double fat;
	@JsonProperty("carbohydrate(g)")
	private Double carbohydrate;

	public static FoodDto toDto(Food food) {
		return FoodDto.builder()
			.id((food.getRid()))
			.name(food.getName())
			.servingUnit(food.getServingUnit())
			.servingSize(food.getServingSize())
			.protein(food.getProtein())
			.energy(food.getEnergy())
			.fat(food.getFat())
			.carbohydrate(food.getCarbohydrate())
			.build();
	}
}
