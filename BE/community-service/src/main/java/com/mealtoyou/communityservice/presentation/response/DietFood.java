package com.mealtoyou.communityservice.presentation.response;

public record DietFood(
        String foodName,

        String foodImageUrl,

        Integer calories
) {
}
