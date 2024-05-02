package com.mealtoyou.foodservice.domain.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Document(indexName = "food")
@Getter
@ToString
public class Food {
	private String code;
	private String name;
	private String origin;
	@Field(name = "major_category")
	@JsonProperty("major_category")
	private String majorCategory;
	@Field(name = "serving_unit")
	@JsonProperty("serving_unit")
	private String servingUnit;
	@Field(name = "protein(g)")
	@JsonProperty("protein(g)")
	private Double protein;
	@Field(name = "sodium(mg)")
	@JsonProperty("sodium(mg)")
	private Double sodium;
	@Field(name = "cholesterol(mg)")
	@JsonProperty("cholesterol(mg)")
	private Double cholesterol;
	@Field(name = "energy(kcal)")
	@JsonProperty("energy(kcal)")
	private Double energy;
	@Field(name = "fat(g)")
	@JsonProperty("fat(g)")
	private Double fat;
	@Field(name = "carbohydrate(g)")
	@JsonProperty("carbohydrate(g)")
	private Double carbohydrate;
	@Field(name = "total_saturated_fatty_acids(g)")
	@JsonProperty("total_saturated_fatty_acids(g)")
	private Double totalSaturatedFattyAcids;
	@Field(name = "serving_size")
	@JsonProperty("serving_size")
	private Double servingSize;
	@Field(name = "total_sugars(g)")
	@JsonProperty("total_sugars(g)")
	private Double totalSugars;
	@Field(name = "total_dietary_fiber(g)")
	@JsonProperty("total_dietary_fiber(g)")
	private Double totalDietaryFiber;
	@Field(name = "total_amino_acids(mg)")
	@JsonProperty("total_amino_acids(mg)")
	private Double totalAminoAcids;
	@Field(name = "trans_fatty_acids(g)")
	@JsonProperty("trans_fatty_acids(g)")
	private Double transFattyAcids;
	@Field(name = "total_fatty_acids(g)")
	@JsonProperty("total_fatty_acids(g)")
	private Double totalFattyAcids;
	@Field(name = "wate(g)r")
	@JsonProperty("wate(g)r")
	private Double water;
	@Field(name = "ash(g)")
	@JsonProperty("ash(g)")
	private Double ash;
	@Field(name = "total_essential_fatty_acids(g)")
	@JsonProperty("total_essential_fatty_acids(g)")
	private Double totalEssentialFattyAcids;

}