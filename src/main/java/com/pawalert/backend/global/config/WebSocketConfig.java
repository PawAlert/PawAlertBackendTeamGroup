package com.pawalert.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 구독할 수 있는 경로 설정
        config.enableSimpleBroker("/queue", "/topic");
        // 클라이언트에서 서버로 메시지를 보낼 때 사용되는 경로 접두사 설정
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 백엔드 서버에 대한 WebSocket 엔드포인트 설정
        registry.addEndpoint("/ws")
                .setAllowedOrigins("https://pawalert.co.kr/")
                .withSockJS(); // SockJS를 사용하여 WebSocket 연결
    }

}
