package com.mealtoyou.healthservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserHealthInfo {

    private int steps;

    private int caloriesBurned;
}
