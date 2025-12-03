package com.shera.framework.employment.employment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务配置
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 消息发送线程池
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数：根据CPU核心数设置
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(corePoolSize);
        
        // 最大线程数：核心线程数的2倍
        executor.setMaxPoolSize(corePoolSize * 2);
        
        // 队列容量：1000个任务
        executor.setQueueCapacity(1000);
        
        // 线程名前缀
        executor.setThreadNamePrefix("message-send-");
        
        // 拒绝策略：由调用线程处理
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 线程空闲时间：60秒
        executor.setKeepAliveSeconds(60);
        
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // 等待时间：60秒
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }
}
