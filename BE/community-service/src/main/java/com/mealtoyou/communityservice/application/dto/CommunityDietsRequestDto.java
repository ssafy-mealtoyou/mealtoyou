package com.mealtoyou.communityservice.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CommunityDietsRequestDto {
    private Long userId;
    private List<Long> dietIdList;
}
