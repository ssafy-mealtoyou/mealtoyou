package com.mealtoyou.chattingservice.presentation.response;

public record DietFood(
        String foodName,

        String foodImageUrl,

        Integer calories
) {
}
