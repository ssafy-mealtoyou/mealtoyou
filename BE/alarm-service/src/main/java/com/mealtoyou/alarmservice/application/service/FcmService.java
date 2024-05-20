package com.mealtoyou.alarmservice.application.service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {

	@Value("${fcm.service-account-file}")
	private String serviceAccountFilePath;

	@Value("${fcm.topic-name}")
	private String topicName;

	@Value("${fcm.project-id}")
	private String projectId;

	@PostConstruct
	public void init() throws IOException {
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(
				GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFilePath).getInputStream()))
			.setProjectId(projectId)
			.build();
		FirebaseApp.initializeApp(options);
	}

	public Mono<Void> sendMessageByToken(String data, String token, Boolean flag) {

		return Mono.fromCallable(() -> {
			try {
				log.info("알림 전송 {} {} {}", data, token, flag.toString());
				if (flag) {
					FirebaseMessaging.getInstance().send(Message.builder()
						.putData("title", "영양제 알림") // 데이터의 키-값 쌍으로 'title'과 'body'를 추가
						.putData("body",
							Arrays.stream(data.split(" ")).skip(1).collect(Collectors.joining(" ")) + " 복용 시간 입니다!")
						.putData("extraInformation", data.split(" ")[0])
						.setToken(token)
						.build());
					System.out.println(LocalTime.now() + token);
				} else {
					FirebaseMessaging.getInstance().send(Message.builder()
						.putData("title", "단식 알림") // 데이터의 키-값 쌍으로 'title'과 'body'를 추가
						.putData("body", "단식 " + data + " 시간 입니다!")
						.setToken(token)
						.build());
					System.out.println(LocalTime.now() + token);
				}
				System.out.println(LocalTime.now() + token);
				return null; // Mono<Void>에 맞춰서 null 반환
			} catch (FirebaseMessagingException e) {
				//TODO: 에러가 발생시 토큰 초기화 or 처리방법 추가
				throw new RuntimeException(e);
			}
		}).then();
	}
}

