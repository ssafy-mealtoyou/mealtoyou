package com.mealtoyou.foodservice.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;

@Table("diet")
@Getter
public class Diet {
	@Id @Column("diet_id")
	private Long dietId;
	@Column("user_id")
	private Long userId;
	@Column("create_datetime")
	private LocalDateTime createDateTime;
	@Column("total_calories")
	private Double totalCalories;
	@Column("total_carbohydrate")
	private Double totalCarbohydrate;
	@Column("total_protein")
	private Double totalProtein;
	@Column("total_fat")
	private Double totalFat;

	@Builder
	public Diet(Long dietId, Long userId, LocalDateTime createDateTime, Double totalCalories, Double totalCarbohydrate,
		Double totalProtein, Double totalFat) {
		this.dietId = dietId;
		this.userId = userId;
		this.createDateTime = createDateTime;
		this.totalCalories = totalCalories;
		this.totalCarbohydrate = totalCarbohydrate;
		this.totalProtein = totalProtein;
		this.totalFat = totalFat;
	}

	public void updateTotalNutrients(Double totalCalories, Double totalCarbohydrate,
		Double totalProtein, Double totalFat) {
		this.totalCalories = totalCalories;
		this.totalCarbohydrate = totalCarbohydrate;
		this.totalProtein = totalProtein;
		this.totalFat = totalFat;
	}
}
