package com.mealtoyou.chattingservice.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Chat {
    @Id
    private String id;
    private Long userId;
    private Long groupId;
    private String message;
    private LocalDateTime timestamp;
}
