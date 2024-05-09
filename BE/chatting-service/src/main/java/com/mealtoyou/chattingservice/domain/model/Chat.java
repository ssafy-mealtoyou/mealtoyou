package com.mealtoyou.chattingservice.domain.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Chat {
    @Id
    private ObjectId id;
    private Long userId;
    private Long groupId;
    private Message message;
    private LocalDateTime timestamp;
}
