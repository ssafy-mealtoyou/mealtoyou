package com.mealtoyou.chattingservice.application.dto;

import com.mealtoyou.chattingservice.domain.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecentMessage {
    private Message message1;
    private Message message2;
    private Message message3;
}
