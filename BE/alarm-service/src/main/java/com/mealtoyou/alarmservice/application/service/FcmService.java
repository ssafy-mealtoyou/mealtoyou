package com.mealtoyou.alarmservice.application.service;

import java.io.IOException;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

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

	public Mono<Void> sendMessageByToken(String title, String body, String token) {
		return Mono.fromCallable(() -> {
			log.info("알림 전송 {} {} {}", title, body, token);
			try {
				FirebaseMessaging.getInstance().send(Message.builder()
					.setNotification(Notification.builder()
						.setTitle(title)
						.setBody(body)
						.build())
					.setToken(token)
					.build());
				System.out.println(LocalTime.now() + token);
				return null; // Mono<Void>에 맞춰서 null 반환
			} catch (FirebaseMessagingException e) {
				//TODO: 에러가 발생시 토큰 초기화 or 처리방법 추가
				throw new RuntimeException(e);
			}
		}).then();
	}
}

