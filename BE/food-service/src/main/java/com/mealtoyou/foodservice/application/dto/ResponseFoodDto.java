package com.mealtoyou.foodservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ResponseFoodDto {
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

    @JsonProperty("similar_energy")
    List<FoodDto> similarEnergy;
    @JsonProperty("similar_protein")
    List<FoodDto> similarProtein;
    @JsonProperty("similar_carbohydrate")
    List<FoodDto> similarCarbohydrate;
    @JsonProperty("similar_fat")
    List<FoodDto> similarFat;

}
