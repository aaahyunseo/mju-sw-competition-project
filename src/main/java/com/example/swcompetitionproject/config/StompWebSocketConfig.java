package com.example.swcompetitionproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker    //웹소켓 지원 활성화
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")   // 클라이언트에서 서버로 WebSocket 연결하기 위해 /ws/chat 으로 요청을 보내도록 엔트포인트 설정
                .setAllowedOriginPatterns("*")  // 클라이언트에서 웹 소켓 서버에 요청하는 모든 요청을 수락, CORS 방지
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 발행하는 요청 url -> 메시지를 보낼 때
        registry.setApplicationDestinationPrefixes(("/pub"));
        // 메시지를 구독하는 요청 url -> 메시지를 받을 때
        registry.enableSimpleBroker("/sub");
    }
}
