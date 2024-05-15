package com.mealtoyou.userservice.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.ToDoubleFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.userservice.application.dto.ChatGPTRequest;
import com.mealtoyou.userservice.application.dto.ChatGPTResponse;
import com.mealtoyou.userservice.application.dto.DailyDietFoodRequestDto;
import com.mealtoyou.userservice.application.dto.DailyDietsResponseDto;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import com.mealtoyou.userservice.infrastructure.config.ChatGPTConfig;
import com.mealtoyou.userservice.infrastructure.kafka.KafkaMonoUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AIFeedbackService {

	@Value("${openai.use}")
	private boolean use;

	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	private final KafkaMonoUtils kafkaMonoUtils;

	private final UserRepository userRepository;

	private Mono<Double> requestBMR(long userId) {
		return kafkaMonoUtils.sendAndReceive("health-service-getBmr", userId).map(Double::parseDouble);
	}

	@Autowired
	public AIFeedbackService(
		@Value("${openai.apikey}") String gptApiKey,
		ObjectMapper objectMapper, KafkaMonoUtils kafkaMonoUtils, UserRepository userRepository
	) {
		this.objectMapper = objectMapper;
		this.webClient = WebClient.builder()
			.baseUrl(ChatGPTConfig.URL)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, ChatGPTConfig.MEDIA_TYPE)
			.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gptApiKey)
			.build();
		this.kafkaMonoUtils = kafkaMonoUtils;
		this.userRepository = userRepository;
	}

	private Mono<ChatGPTRequest> buildRequestBody(Long userId) {
		return createMessages(userId).map(m -> {
				return ChatGPTRequest.builder()
					.model(ChatGPTConfig.MODEL)
					.maxTokens(ChatGPTConfig.MAX_TOKEN)
					.temperature(ChatGPTConfig.TEMPERATURE)
					.messages(m)
					.build();
			}
		);
	}

	private Mono<DailyDietsResponseDto> requestTodayDietsInfo(long userId, LocalDate date) {
		DailyDietFoodRequestDto dailyDietFoodRequestDto = DailyDietFoodRequestDto.builder()
			.userId(userId)
			.date(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
			.build();
		return kafkaMonoUtils.sendAndReceive("food-service-getFoodListByDate", dailyDietFoodRequestDto)
			.map(message -> {
				try {
					return objectMapper.readValue(message, DailyDietsResponseDto.class);
				} catch (JsonProcessingException e) {
					return DailyDietsResponseDto.builder().build();
				}
			});
	}

	private Mono<ArrayList<ChatGPTRequest.ChatGptMessage>> createMessages(Long userId) {

		LocalDate today = LocalDate.now();

		Mono<User> userMono = userRepository.findById(userId);
		Mono<Double> bmrMono = requestBMR(userId);
		Mono<NutritionSummaryDto> nutritionSummaryDtoMono = Mono.zip(requestTodayDietsInfo(userId, today),
				requestTodayDietsInfo(userId, today.minusDays(1)),
				requestTodayDietsInfo(userId, today.minusDays(2)))
			.map(tuple -> Arrays.asList(tuple.getT1(), tuple.getT2(), tuple.getT3()))
			.map(list -> {

				int cntDays = 0;
				double totalCaloriesBurned = 0;
				double totalCarbohydrateTaked = 0.0;
				double totalProteinTaked = 0.0;
				double totalFatTaked = 0.0;

				for (var diet : list) {
					if (diet.diets() == null || diet.diets().isEmpty()) {
						continue;
					}
					cntDays += 1;
					totalCaloriesBurned += diet.dailyCaloriesBurned();
					totalCarbohydrateTaked += diet.dailyCarbohydrateTaked();
					totalProteinTaked += diet.dailyProteinTaked();
					totalFatTaked += diet.dailyFatTaked();
				}

				if (cntDays > 0) {
					log.info("유효한 식단의 일수: {}", cntDays);

					// 유효한 일수 동안의 평균 영양소 섭취량 계산
					double avgCarbohydrateTaked = totalCarbohydrateTaked / cntDays;
					double avgProteinTaked = totalProteinTaked / cntDays;
					double avgFatTaked = totalFatTaked / cntDays;

					// 평균 영양소별 칼로리 계산
					double avgCarbohydrateCalories = avgCarbohydrateTaked * 4;
					double avgProteinCalories = avgProteinTaked * 4;
					double avgFatCalories = avgFatTaked * 9;

					// 총 평균 섭취 칼로리 계산
					double totalAvgIntakeCalories = avgCarbohydrateCalories + avgProteinCalories + avgFatCalories;

					log.info("평균 탄수화물 섭취 칼로리: {}, 평균 단백질 섭취 칼로리: {}, 평균 지방 섭취 칼로리: {}", avgCarbohydrateCalories, avgProteinCalories, avgFatCalories);

					// 각 영양소의 퍼센테이지 계산
					double carbohydratePercentage = (avgCarbohydrateCalories / totalAvgIntakeCalories) * 100;
					double proteinPercentage = (avgProteinCalories / totalAvgIntakeCalories) * 100;
					double fatPercentage = (avgFatCalories / totalAvgIntakeCalories) * 100;

					log.info("탄수화물 퍼센테이지: {}, 단백질 퍼센테이지: {}, 지방 퍼센테이지: {}", carbohydratePercentage, proteinPercentage, fatPercentage);

					// 결과 DTO 생성 및 반환
					return NutritionSummaryDto.builder()
						.isEmpty(false)
						.totalCalories(totalAvgIntakeCalories)
						.carbohydratePercentage(carbohydratePercentage)
						.proteinPercentage(proteinPercentage)
						.fatPercentage(fatPercentage)
						.build();
				} else {
					log.info("유효한 식단이 없습니다.");
					// 유효한 식단이 없을 때의 처리
					return NutritionSummaryDto.builder().isEmpty(true).build();
				}

			});

		return Mono.zip(userMono, bmrMono, nutritionSummaryDtoMono)
			.map(a -> {
				User user = a.getT1();
				Double bmr = a.getT2();
				NutritionSummaryDto nutritionSummaryDto = a.getT3();

				UserDietAnalysisRequestDto resultDto;
				UserDietAnalysisRequestDto.UserDietAnalysisRequestDtoBuilder dtoBuilder =
					UserDietAnalysisRequestDto.builder()
					.age(user.getAge())
					.gender(user.isGender() ? "여자" : "남자")
					.weight(user.getWeight())
					.height(user.getHeight())
					.bmi(user.getWeight() / Math.pow(user.getHeight() / 100.0, 2))
					.bmr(bmr);

				String dietInfoMessage;
				if (nutritionSummaryDto.getIsEmpty()) {
					resultDto = dtoBuilder.build();
					dietInfoMessage = "지난 주 내 식단 정보: 등록되지 않음.";
				} else {
					resultDto = dtoBuilder.averageDailyCalorieIntake(nutritionSummaryDto.getTotalCalories())
						.carbohydratePercentage(nutritionSummaryDto.getCarbohydratePercentage())
						.proteinPercentage(nutritionSummaryDto.getProteinPercentage())
						.fatPercentage(nutritionSummaryDto.getFatPercentage())
						.build();
					dietInfoMessage = String.format(
						"지난 주 내 식단 정보: 하루 평균 칼로리 섭취량 %.0fkcal, 탄수화물 %.0f%%, 단백질 %.0f%%, 지방 %.0f%%",
						resultDto.getAverageDailyCalorieIntake(), resultDto.getCarbohydratePercentage(), resultDto.getProteinPercentage(),
						resultDto.getFatPercentage());
				}

				// 각 메시지를 포맷팅
				String systemMessage = "이 AI는 사용자의 나이, 성별, 체중, 키, BMI, 기초 대사량과 과거 식단 정보를 바탕으로 주간 식단 분석 피드백을 제공한다.";
				String userInfoMessage = String.format(
					"나이: %d, 성별: %s, 체중: %.0fkg, 키: %.0fcm, BMI: %.1f, 기초 대사량: %.1fkcal",
					resultDto.getAge(), resultDto.getGender(), resultDto.getWeight(), resultDto.getHeight(), resultDto.getBmi(), resultDto.getBmr());
				String requestFeedback = "지난 주 식단에 대한 분석과 이번 주 식단 조정에 대한 피드백을 간단한 제목과 내용을 줘 "
					+ "제목은 3단어 내외이고, 내용은 2~3문장으로, 예시는 다음과 같아 {\"title\": \"탄수화물 과다\", \"content\": \"내용 예시\"}";


				ArrayList<ChatGPTRequest.ChatGptMessage> messages = new ArrayList<>(
					List.of(
						createMessage(ChatGPTRequest.Role.SYSTEM, systemMessage),
						createMessage(ChatGPTRequest.Role.USER, userInfoMessage),
						createMessage(ChatGPTRequest.Role.USER, dietInfoMessage),
						createMessage(ChatGPTRequest.Role.USER, requestFeedback)
					)
				);

				log.info(String.valueOf(messages));

				return messages;
			});

	}

	private ChatGPTRequest.ChatGptMessage createMessage(ChatGPTRequest.Role role, String content) {
		return ChatGPTRequest.ChatGptMessage.builder()
			.role(role)
			.content(content)
			.build();
	}

	private Mono<String> parseResponse(String body) {
		try {
			ChatGPTResponse chatGPTResponse = objectMapper.readValue(body, ChatGPTResponse.class);
			return Mono.just(chatGPTResponse.getChoices().get(0).getMessage().getContent());
		} catch (Exception e) {
			return Mono.error(e);
		}
	}

	public Mono<String> generateAIFeedback(Long userId) {
		if (!use) {
			log.debug("AI 피드백 OFF");
			return Mono.just("OFF");
		}
		Mono<ChatGPTRequest> chatGPTRequestMono = buildRequestBody(userId);
		return chatGPTRequestMono.flatMap(body ->
			this.webClient.post()
				.bodyValue(body)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
					clientResponse.bodyToMono(String.class)
						.flatMap(errorMessage -> Mono.error(new Exception("4xx error occurred: " + errorMessage)))
				)
				.onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
					clientResponse.bodyToMono(String.class)
						.flatMap(errorMessage -> Mono.error(new Exception("5xx error occurred: " + errorMessage)))
				)
				.bodyToMono(String.class)
				.flatMap(this::parseResponse)
				.onErrorResume(e -> {
					log.error("AI 피드백 요청중 에러 발생", e);
					return Mono.just("");
				})
		);
	}

}

@Getter
@ToString
@Builder
class UserDietAnalysisRequestDto {
	private Long userId;
	private Integer age;
	private String gender;
	private Double weight;
	private Double height;
	private Double bmi;
	private Double bmr;
	private Double averageDailyCalorieIntake;
	private Double carbohydratePercentage;
	private Double proteinPercentage;
	private Double fatPercentage;
}

@Getter
@Builder
@ToString
class NutritionSummaryDto {
	private Boolean isEmpty;
	private Double totalCalories;
	private Double carbohydratePercentage;
	private Double proteinPercentage;
	private Double fatPercentage;
}
