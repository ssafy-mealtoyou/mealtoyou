package com.mealtoyou.userservice.application.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatGPTRequest implements Serializable {
	private String model;
	//    private String prompt;
	@JsonProperty("max_tokens")
	private Integer maxTokens;
	private Double temperature;
	private Boolean stream;
	private List<ChatGptMessage> messages;
	//    @JsonProperty("top_p")
	//    private Double topP;
	@JsonProperty("response_format")
	private ResponseFormat responseFormat;

	@Getter
	@Builder
	public static class ResponseFormat {
		private String Type;
	}

	@Getter
	@Builder
	public static class ChatGptMessage implements Serializable {
		private Role role;
		private String content;
	}

	public enum Role {
		SYSTEM("system"),
		ADMIN("admin"),
		USER("user"),
		GUEST("guest");

		// 역할의 문자열 식별자를 반환하는 메소드
		private final String role;

		// enum 생성자 - 각 역할의 문자열 식별자를 설정합니다.
		Role(String role) {
			this.role = role;
		}

		@JsonValue
		public String getRole() {
			return role;
		}

		@JsonCreator
		public static Role forValue(String value) {
			for (Role role : Role.values()) {
				if (role.getRole().equals(value)) {
					return role;
				}
			}
			throw new IllegalArgumentException("Invalid role value: " + value);
		}
	}

}
