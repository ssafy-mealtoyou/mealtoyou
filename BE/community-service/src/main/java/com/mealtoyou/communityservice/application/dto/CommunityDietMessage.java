package com.mealtoyou.communityservice.application.dto;

import com.mealtoyou.communityservice.presentation.response.DietFood;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommunityDietMessage implements Message {

    private Long dietId;

    private String nickname;

    private Integer totalCalories;

    private Integer carbohydratePer;

    private Integer proteinPer;

    private Integer fatPer;

    private List<DietFood> foodList;
}
