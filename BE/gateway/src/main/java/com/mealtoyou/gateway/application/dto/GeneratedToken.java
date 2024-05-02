package com.mealtoyou.gateway.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GeneratedToken {

    private String grantType;

    private String accessToken;

    private String refreshToken;

}