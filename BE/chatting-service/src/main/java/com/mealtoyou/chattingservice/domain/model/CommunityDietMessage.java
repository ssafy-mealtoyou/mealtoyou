package com.mealtoyou.chattingservice.domain.model;

import com.mealtoyou.chattingservice.application.dto.DailyDietsResponseDto;
import com.mealtoyou.chattingservice.presentation.response.DietFood;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommunityDietMessage implements Message {
    private DailyDietsResponseDto dailyDietsResponseDto;
}
