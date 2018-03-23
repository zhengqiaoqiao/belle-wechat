package com.belle.wechat.framework.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfiguration {
	@Bean
	public RedisTemplate<?, ?> redisTemplate(RedisTemplate<?, ?> redisTemplate) {
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
		return redisTemplate;
	}
	
}
