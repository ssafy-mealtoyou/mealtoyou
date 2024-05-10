package com.mealtoyou.chattingservice.infrastructure.config;

import com.mealtoyou.chattingservice.infrastructure.handler.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig implements WebFluxConfigurer {

    @Bean
    public HandlerMapping webSocketHandlerMapping(ChatWebSocketHandler handler)  {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/chat/{group}", handler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public MessageChannel clientOutboundChannel() {
        return new ExecutorSubscribableChannel();
    }

    @Bean
    public SimpMessagingTemplate messagingTemplate() {
        return new SimpMessagingTemplate(clientOutboundChannel());
    }
}