package com.mealtoyou.chattingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TextMessage implements Message {
    private String message;
}
