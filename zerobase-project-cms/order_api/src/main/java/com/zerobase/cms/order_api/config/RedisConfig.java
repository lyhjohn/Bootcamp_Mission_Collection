package com.zerobase.cms.order_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		// serializetion(직렬화): 객체를 옮기기 쉬운 형태로 변환
		// deserializetion(역직렬화): 직렬화와 반대. 스트림->객체로 재구성
		
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		StringRedisSerializer serializer = new StringRedisSerializer();
		/**
		 * Spring - Redis 간 데이터 직렬화, 역직렬화 시 사용하는 방식이 JDK 직렬화 방식이다.
		 * 동작에는 문제가 없지만 redis-cli를 통해 데이터를 보려고 할 때 알아볼 수 없는 형태로 출력되기 때문에 적용하는 설정.
		 */
		redisTemplate.setKeySerializer(serializer);
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(serializer);
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}
}
