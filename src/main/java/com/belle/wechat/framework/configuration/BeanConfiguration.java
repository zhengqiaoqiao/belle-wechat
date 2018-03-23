package com.belle.wechat.framework.configuration;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BeanConfiguration {
	private final static Logger LOGGER = LoggerFactory.getLogger(BeanConfiguration.class);

    @Bean(name="taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(3);
        //最大线程数
        executor.setMaxPoolSize(30);
        //队列最大长度
        executor.setQueueCapacity(50);
        //线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(1800);
        //CallerRunsPolicy是“调用者运行”策略，实现了一种调节机制 。它不会抛弃任务，也不会抛出异常。 而是将任务回退到调用者。它不会在线程池中执行任务，而是在一个调用了Executor的线程中执行该任务。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        LOGGER.info("初始化线程池完成！");
        return executor;
    }

}
