package com.mealtoyou.foodservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("diet_food")
@Getter
public class DietFood {
	@Id @Column("diet_food_id")
	private Long dietFoodId;
	@Column("diet_id")
	@Setter
	private Long dietId;
	@Column("food_id")
	private Long foodId;
	@Column("serving_size")
	private Double servingSize;

	@Builder
	public DietFood(Long dietFoodId, Long dietId, Long foodId, Double servingSize) {
		this.dietFoodId = dietFoodId;
		this.dietId = dietId;
		this.foodId = foodId;
		this.servingSize = servingSize;
	}
}
