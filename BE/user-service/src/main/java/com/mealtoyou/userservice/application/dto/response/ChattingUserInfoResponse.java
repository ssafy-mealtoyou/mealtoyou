package com.mealtoyou.userservice.application.dto.response;

public record ChattingUserInfoResponse (
        Long userId,
        String nickname,
        String profileImageUrl
) {
}