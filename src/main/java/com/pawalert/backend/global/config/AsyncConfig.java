package com.pawalert.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // 동시에 실행할 쓰레드 수
        executor.setMaxPoolSize(10);  // 최대 쓰레드 수
        executor.setQueueCapacity(500);  // 대기 작업 큐 크기
        executor.setThreadNamePrefix("AsyncExecutor-");  // 쓰레드 이름 접두사
        executor.initialize();
        return executor;
    }
}
