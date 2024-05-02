package com.mealtoyou.userservice.application.dto;

import com.mealtoyou.userservice.domain.model.User;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(attributes);
        } else {
            throw new RuntimeException("cannot find registrationId");
        }
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .build();
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(name)
                .socialKey("google")
                .userImageUrl(profile)
                .role("USER")
                .build();
    }

}
