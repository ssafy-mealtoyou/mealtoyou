package com.mealtoyou.communityservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TextMessage implements Message {
    private String message;
}
