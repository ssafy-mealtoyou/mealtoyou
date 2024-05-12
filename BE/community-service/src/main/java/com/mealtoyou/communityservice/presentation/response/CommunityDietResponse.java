package com.mealtoyou.communityservice.presentation.response;

import java.util.List;

public record CommunityDietResponse(
        Long dietId,

        Integer totalCalories,

        Integer carbohydratePer,

        Integer proteinPer,

        Integer fatPer,

        List<DietFood> dietFoods
) {
}
