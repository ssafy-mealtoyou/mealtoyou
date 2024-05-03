package com.mealtoyou.chattingservice.infrastructure.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtoyou.chattingservice.application.service.ChatCacheService;
import com.mealtoyou.chattingservice.application.service.JwtTokenProvider;
import com.mealtoyou.chattingservice.application.service.RouteService;
import com.mealtoyou.chattingservice.domain.model.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {
    private final RouteService routeService;
    private final Map<Long, List<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatCacheService chatCacheService;
    private final ObjectMapper objectMapper;

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull WebSocketSession session) {
        Long groupId = parseGroupFromWebSocketAddress(String.valueOf(session.getHandshakeInfo().getUri()));
        if (groupId == null) {
            log.warn("WebSocket 주소에서 그룹 정보를 찾을 수 없습니다.");
            return Mono.empty();
        }

        groupSessions.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(session);

        // Send previous chats first
        Mono<Void> sendPreviousChats = sendPreviousChatsToUser(groupId, session);

        return sendPreviousChats
                .thenMany(session.receive()
                        .flatMap(message -> handleMessage(session, message, groupId, session.getId()))
                        .doFinally(signalType -> {
                            if (signalType == SignalType.CANCEL) {
                                log.info("Client terminated the connection");
                                removeSessionFromGroup(groupId, session);
                            }
                        }))
                .then(Mono.fromRunnable(() -> removeSessionFromGroup(groupId, session)))
                .onErrorResume(throwable -> {
                    log.warn("Error during WebSocket handling: {}", throwable.getMessage());
                    return Mono.empty();
                }).then();
    }

    public Mono<Void> sendPreviousChatsToUser(Long groupId, WebSocketSession session) {
        log.info("채팅 조회");
        return chatCacheService.getRecentChatsFromMongo(groupId, 20)
                .doOnNext(chats -> log.info("채팅 조회 결과: {}", chats)) // Log the fetched chats
                .flatMapMany(Flux::fromIterable)
                .flatMap(chat -> session.send(Mono.just(session.textMessage(chat.getMessage()))))
                .then();
    }

    private Mono<Void> handleMessage(WebSocketSession session, WebSocketMessage message, Long groupId, String senderSessionId) {
        log.info("클라이언트로부터 메시지 수신: {}", message.getPayloadAsText());

        if (message.getPayloadAsText().equals("CLOSE_MESSAGE")) {
            return session.close();
        }

        Long userId = getUserIdFromSession(session);

        Chat chat;
        try {
            chat = objectMapper.readValue(message.getPayloadAsText(), Chat.class);
        } catch (IOException e) {
            log.error("Error deserializing Chat object: {}", e.getMessage());
            return Mono.error(e);
        }
        chat.setUserId(userId);
        chat.setGroupId(groupId);
        chat.setTimestamp(LocalDateTime.now());

        return routeService.route(chat)
                .flatMap(savedMessage -> broadcastMessageToGroup(chat, senderSessionId))
                .then()
                .onErrorResume(throwable -> {
                    log.warn("Error during message handling: {}", throwable.getMessage());
                    return Mono.empty();
                });

    }

    private Mono<Void> broadcastMessageToGroup(Chat chat, String senderSessionId) {
        List<WebSocketSession> sessions = groupSessions.getOrDefault(chat.getGroupId(), Collections.emptyList());

        return Flux.fromIterable(sessions)
                .filter(session -> !session.getId().equals(senderSessionId))
                .flatMap(session -> {
                    if (session.isOpen()) {
                        String jsonMessage = convertChatToJson(chat);
                        return session.send(Mono.just(session.textMessage(jsonMessage)))
                                .then()
                                .doOnSuccess(success -> log.info("Message sent successfully to session {}", session.getId()))
                                .doOnError(error -> log.error("Error sending message to session {}: {}", session.getId(), error.getMessage()))
                                .onErrorResume(error -> Mono.empty()); // Continue with empty Mono on error
                    } else {
                        log.warn("Session {} is closed. Skipping message send operation.", session.getId());
                        return Mono.empty();
                    }
                })
                .then();
    }


    private String convertChatToJson(Chat chat) {
        try {
            return objectMapper.writeValueAsString(chat);
        } catch (JsonProcessingException e) {
            log.error("Error converting chat to JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting chat to JSON");
        }
    }

    private void removeSessionFromGroup(Long groupId, WebSocketSession session) {
        List<WebSocketSession> sessions = groupSessions.get(groupId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                groupSessions.remove(groupId);
            }
        }
    }

    private Long parseGroupFromWebSocketAddress(String webSocketAddress) {
        if (webSocketAddress.contains("/chat/group:")) {
            int startIndex = webSocketAddress.indexOf("/chat/group:") + "/chat/group:".length();
            int endIndex = webSocketAddress.indexOf("/", startIndex);
            if (endIndex == -1) {
                endIndex = webSocketAddress.length();
            }
            return Long.valueOf(webSocketAddress.substring(startIndex, endIndex));
        } else {
            return null;
        }
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        String token = session.getHandshakeInfo().getHeaders().getFirst("token");
        if (token == null) {
            throw new RuntimeException("token 조회 실패");
        }
        return jwtTokenProvider.getUserId(token);
    }
}
