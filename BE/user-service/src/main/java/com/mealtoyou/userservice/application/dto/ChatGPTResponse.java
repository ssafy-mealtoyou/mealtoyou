package com.mealtoyou.userservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatGPTResponse {
	private List<Choice> choices;
	private Integer created;
	private String id;
	private String model;
	private String object;
	private Usage usage;

	@Getter
	@Builder
	public static class Choice {
		@JsonProperty("finish_reason")
		private String finishReason;
		private Integer index;
		private Message message;
		private String logprobs;

	}

	@Getter
	@Builder
	public static class Message {
		private String content;
		private String role;
	}

	@Getter
	@Builder
	public static class Usage {
		@JsonProperty("completion_tokens")
		private Integer completionTokens;
		@JsonProperty("prompt_tokens")
		private Integer promptTokens;
		@JsonProperty("total_tokens")
		private Integer totalTokens;
	}
}

