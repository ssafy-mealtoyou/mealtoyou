package com.mealtoyou.userservice.infrastructure.config;

public class ChatGPTConfig {
	public static final String MODEL = "gpt-3.5-turbo"; // gpt 모델
	public static final Integer MAX_TOKEN = 1000; // 내용 생성할 때, 토큰의 최대값
	public static final Double TEMPERATURE = 0.5; // 0~2 사이의 값으로 설정,
	public static final Boolean STREAM = false;
	public static final String ROLE = "user";
	// 높을수록, 모델이 생성되는 답변이 다양해짐
	//    public static final Double TOP_P = 1.0; // 생성된 텍스트에서 모델이 고려할 후보 단어 집합의 크기를 결정하는 값
	public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
	public static final String URL = "https://api.openai.com/v1/chat/completions";
}
