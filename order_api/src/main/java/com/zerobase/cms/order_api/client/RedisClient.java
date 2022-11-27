package com.zerobase.cms.order_api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order_api.domain.redis.Cart;
import com.zerobase.cms.order_api.exception.CustomException;
import com.zerobase.cms.order_api.exception.ErrorCode;
import io.netty.util.internal.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.zerobase.cms.order_api.exception.ErrorCode.CART_ADD_FAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

    // RedisConfig 파일에 Bean으로 등록해서 관리중임
    private final RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    // 바깥에 toString이 노출되는 것을 꺼려하기 때문에 이런식으로 Long으로 받아서 내부에서 String으로 변환해줌
    public <T> T get(Long key, Class<T> classType) {
        return get(key.toString(), classType);
    }

    private <T> T get(String key, Class<T> classType) {
        // String을 쉽게 Serialize / Deserialize 해주는 Interface
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(redisValue)) {
            return null;
        } else {
            try {
                return mapper.readValue(redisValue, classType);
            } catch (JsonProcessingException e) {
                log.error("Parsing error", e);
                return null;
            }
        }
    }

    // 바깥에 toString이 노출되는 것을 꺼려하기 때문에 이런식으로 Long으로 받아서 내부에서 String으로 변환해줌
    public void put(Long key, Cart cart) {
        put(key.toString(), cart);
    }

    private void put(String key, Cart cart) {
        try {
            redisTemplate.opsForValue().set(key, mapper.writeValueAsString(cart));
        } catch (JsonProcessingException e) {
            throw new CustomException(CART_ADD_FAIL);
        }
    }
}
