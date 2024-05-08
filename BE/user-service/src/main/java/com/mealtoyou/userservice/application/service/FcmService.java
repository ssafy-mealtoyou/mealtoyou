package com.mealtoyou.userservice.application.service;

import java.io.IOException;

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

@Service
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
		//Firebase 프로젝트 정보를 FireBaseOptions에 입력해준다.
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(
				GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFilePath).getInputStream()))
			.setProjectId(projectId)
			.build();

		//입력한 정보를 이용하여 initialze 해준다.
		FirebaseApp.initializeApp(options);
	}

	public void sendMessageByTopic(String title, String body) throws IOException, FirebaseMessagingException {
		FirebaseMessaging.getInstance().send(Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setTopic(topicName)
			.build());

	}

	public void sendMessageByToken(String title, String body, String token) throws FirebaseMessagingException {
		FirebaseMessaging.getInstance().send(Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setToken(token)
			.build());
	}

}
