package com.mealtoyou.communityservice.presentation.response;

import java.util.List;

public record CommunityDietResponse(
        String nickname,

        Integer totalCalories,

        Integer carbohydratePer,

        Integer proteinPer,

        Integer fatPer,

        List<DietFood> foodList
) {
}
