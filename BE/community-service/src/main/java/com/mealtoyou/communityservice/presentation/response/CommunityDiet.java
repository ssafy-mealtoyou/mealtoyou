package com.mealtoyou.communityservice.presentation.response;

import java.util.List;

public record CommunityDiet(
        String nickname,

        Integer carbohydratePer,

        Integer proteinPer,

        Integer fatPer,

        List<DietFood> foodList
) {
}
