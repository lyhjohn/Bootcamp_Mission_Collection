package zerobase.stock.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Bean
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		// Serialization(직렬화)을 통해 자바 데이터가 외부에서도 호환되도록 바이트 형태로 변환 <-> 역직렬화
		// Date 타입은 직렬화 시 어떤 규칙으로 바이트화 할건지 어노테이션을 붙여줘야함
		// 	@JsonSerialize(using = LocalDateTimeSerializer.class)
		//	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
		RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new GenericJackson2JsonRedisSerializer()));
		return RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(redisConnectionFactory)
			.cacheDefaults(conf)
			.build();
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
//		RedisClusterConnection // 레디스 서버 구성 방식에 따라 클러스터/스탠드 등 선택
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
		conf.setHostName(host);
		conf.setPort(port);
		return new LettuceConnectionFactory(conf);
	}
}
