package com.learning.BookApi.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableCaching
public class BookApiConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public RedisCacheManager cacheManager (RedisConnectionFactory connectionFactory){

        //setting up default configuration for external api bucket
        RedisCacheConfiguration stagingConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new JdkSerializationRedisSerializer()));

        //setting up default configuration for reviewedBook list
        RedisCacheConfiguration reviewConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new JdkSerializationRedisSerializer()));

        // Build the Manager and register the specific configuration
        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration("externalBookStaging", stagingConfig)
                .withCacheConfiguration("reviewedBooks", reviewConfig)
                .build();
    }
}
