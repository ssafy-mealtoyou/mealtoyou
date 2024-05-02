package com.mealtoyou.chattingservice.infrastructure.handler;

import com.mealtoyou.chattingservice.application.service.JwtTokenProvider;
import com.mealtoyou.chattingservice.application.service.RouteService;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {
    private final RouteService routeService; // 메시지 라우팅을 처리하는 서비스
    private final Map<Long, List<WebSocketSession>> groupSessions = new ConcurrentHashMap<>(); // 각 그룹에 대한 WebSocket 세션 목록을 관리하는 맵
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull WebSocketSession session) {
        Long groupId = parseGroupFromWebSocketAddress(String.valueOf(session.getHandshakeInfo().getUri()));
        if (groupId == null) {
            log.warn("WebSocket 주소에서 그룹 정보를 찾을 수 없습니다.");
            return Mono.empty();
        }

        groupSessions.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(session);

        return session
                .receive()
                .flatMap(message -> handleMessage(session, message, groupId, session.getId()))
                .then(Mono.fromRunnable(() -> removeSessionFromGroup(groupId, session)))
                .onErrorResume(throwable -> {
                    log.warn("Error during WebSocket handling: {}", throwable.getMessage());
                    return Mono.empty();
                })
                .doFinally(signalType -> {
                    if (signalType == SignalType.CANCEL) {
                        // Handle user-initiated disconnection
                        log.info("Client terminated the connection");
                        removeSessionFromGroup(groupId, session);
                    }
                }).then();
    }


    // 클라이언트로부터 수신된 메시지 처리
    private Mono<Void> handleMessage(WebSocketSession session, WebSocketMessage message, Long groupId, String senderSessionId) {
        log.info("클라이언트로부터 메시지 수신: {}", message.getPayloadAsText());

        // 클라이언트가 "CLOSE_MESSAGE"를 보내면 연결 종료
        if (message.getPayloadAsText().equals("CLOSE_MESSAGE")) {
            return session.close();
        }

        // userId 가져와서 WebSocket 세션에 저장
        Long userId = getUserIdFromSession(session); // 여기에서 userId 가져오는 메소드를 호출하세요.

        // 그룹 내에서 메시지 라우팅
        return routeService.route(message.getPayloadAsText(), userId, groupId)
                .flatMap(savedMessage -> broadcastMessageToGroup(groupId, savedMessage, senderSessionId)) // 그룹 내 모든 클라이언트에게 메시지 전송
                .then();
    }

    // 그룹 내 모든 클라이언트에게 메시지 전송
    private Mono<Void> broadcastMessageToGroup(Long groupId, String message, String senderSessionId) {
        List<WebSocketSession> sessions = groupSessions.getOrDefault(groupId, Collections.emptyList());

        return Flux.fromIterable(sessions)
                .filter(s -> !s.getId().equals(senderSessionId)) // 보낸 클라이언트를 제외하고
                .flatMap(s -> s.send(Mono.just(s.textMessage(message)))) // 메시지 전송
                .then(); // Flux<Void>를 Mono<Void>로 변환
    }

    // 그룹에서 세션 제거
    private void removeSessionFromGroup(Long groupId, WebSocketSession session) {
        List<WebSocketSession> sessions = groupSessions.get(groupId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                groupSessions.remove(groupId);
            }
        }
    }

    // 웹소켓 주소에서 그룹 정보 추출
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
        // 여기에서 userId를 가져오는 로직을 구현하세요.
        // 예를 들어, 세션의 특정 헤더에서 userId를 가져오거나, 인증된 사용자 객체에서 userId를 추출할 수 있습니다.
        // 이 예제에서는 헤더에서 userId를 가져오는 것으로 가정합니다.
        String token = session.getHandshakeInfo().getHeaders().getFirst("token");
        if(token == null) {
            throw new RuntimeException("token 조회 실패");
        }
        return jwtTokenProvider.getUserId(token);
    }

}
