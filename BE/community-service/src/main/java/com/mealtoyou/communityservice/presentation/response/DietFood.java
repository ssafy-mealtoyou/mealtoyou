package com.mealtoyou.communityservice.presentation.response;

public record DietFood(
        String name,
        String imageUrl,
        Double calories,
        Double carbohydrate,
        Double protein,
        Double fat
) {
}
