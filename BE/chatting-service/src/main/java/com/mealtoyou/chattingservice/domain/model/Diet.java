package com.mealtoyou.chattingservice.domain.model;

import com.mealtoyou.chattingservice.presentation.response.DietFood;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Diet {

    private Long dietId;

    private String nickname;

    private Integer totalCalories;

    private Integer carbohydratePer;

    private Integer proteinPer;

    private Integer fatPer;

    private List<DietFood> foodList;
}
