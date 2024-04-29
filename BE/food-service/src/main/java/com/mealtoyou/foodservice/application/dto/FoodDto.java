package com.mealtoyou.foodservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mealtoyou.foodservice.domain.model.Food;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FoodDto {
    private String code;
    private String name;
    private String origin;
    @JsonProperty("major_category")
    private String majorCategory;
    @JsonProperty("serving_unit")
    private String servingUnit;
    @JsonProperty("protein(g)")
    private Double protein;
    @JsonProperty("sodium(mg)")
    private Double sodium;
    @JsonProperty("cholesterol(mg)")
    private Double cholesterol;
    @JsonProperty("energy(kcal)")
    private Double energy;
    @JsonProperty("fat(g)")
    private Double fat;
    @JsonProperty("carbohydrate(g)")
    private Double carbohydrate;
    @JsonProperty("total_saturated_fatty_acids(g)")
    private Double totalSaturatedFattyAcids;
    @JsonProperty("serving_size")
    private Double servingSize;
    @JsonProperty("total_sugars(g)")
    private Double totalSugars;
    @JsonProperty("total_dietary_fiber(g)")
    private Double totalDietaryFiber;
    @JsonProperty("total_amino_acids(mg)")
    private Double totalAminoAcids;
    @JsonProperty("trans_fatty_acids(g)")
    private Double transFattyAcids;
    @JsonProperty("total_fatty_acids(g)")
    private Double totalFattyAcids;
    @JsonProperty("water(g)")
    private Double water;
    @JsonProperty("ash(g)")
    private Double ash;
    @JsonProperty("total_essential_fatty_acids(g)")
    private Double totalEssentialFattyAcids;

    public static FoodDto toDto(Food food){
        return FoodDto.builder()
            .code(food.getCode())
            .name(food.getName())
            .origin(food.getOrigin())
            .majorCategory(food.getMajorCategory())
            .servingUnit(food.getServingUnit())
            .protein(food.getProtein())
            .sodium(food.getSodium())
            .cholesterol(food.getCholesterol())
            .energy(food.getEnergy())
            .fat(food.getFat())
            .carbohydrate(food.getCarbohydrate())
            .totalSaturatedFattyAcids(food.getTotalSaturatedFattyAcids())
            .servingSize(food.getServingSize())
            .totalSugars(food.getTotalSugars())
            .totalDietaryFiber(food.getTotalDietaryFiber())
            .totalAminoAcids(food.getTotalAminoAcids())
            .transFattyAcids(food.getTotalFattyAcids())
            .totalFattyAcids(food.getTotalFattyAcids())
            .water(food.getWater())
            .ash(food.getAsh())
            .totalEssentialFattyAcids(food.getTotalEssentialFattyAcids())
            .build();
    }

}
