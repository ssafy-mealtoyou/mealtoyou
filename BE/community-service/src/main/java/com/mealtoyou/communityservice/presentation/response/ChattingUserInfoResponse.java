package com.mealtoyou.communityservice.presentation.response;

public record ChattingUserInfoResponse(
        Long userId,
        String nickname,
        String profileImageUrl
) {
}
