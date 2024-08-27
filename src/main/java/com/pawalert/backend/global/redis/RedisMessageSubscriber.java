package com.pawalert.backend.global.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageSubscriber implements MessageListener {

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        // 메시지를 수신하면 처리하는 로직을 여기에 추가
        String channel = new String(pattern);
        String messageBody = new String(message.getBody());
        System.out.println("Received message: " + messageBody + " from channel: " + channel);

        // 예를 들어, WebSocket 등을 통해 실시간 알림을 전달
    }
}
