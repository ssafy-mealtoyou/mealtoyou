package com.mealtoyou.userservice.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HealthInfoResponseDto {
  private String nickname;
  private String imageUrl;
  private double inbodyBoneMuscle;
  private double inbodyBodyFat;
  private boolean intermittentYn;
  private LocalTime intermittentStartTime;
  private LocalTime intermittentEndTime;
  private double weight;
  private double weightLastMonth;
  private double weightThisYear;
  private Integer goalWeight;
  private LocalDate goalDate;
}