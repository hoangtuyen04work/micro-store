package com.shop.product_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.product_service.dto.PageResponse;
import com.shop.product_service.dto.ProductResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProductRedisService {
    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper redisObjectMapper;
    public PageResponse<ProductResponse> find(String name, String code, Pageable pageable) throws JsonProcessingException{
        String key = getKeyForm(name, code, pageable);
        String json = (String)redisTemplate.opsForValue().get(key);
        return json == null ? null :
                redisObjectMapper.readValue(json, new TypeReference<PageResponse<ProductResponse>>() {});
    }
    public void saveFindProduct(PageResponse<ProductResponse> response, String  name, String code, Pageable pageable) throws JsonProcessingException {
        String key = getKeyForm(name, code, pageable);
        String json = redisObjectMapper.writeValueAsString(response);
        redisTemplate.opsForValue().set(key, json);
    }
    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    private String getKeyForm(String name, String code, Pageable pageable){
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        return String.format("find_products:%s:%s:%d:%d", name, code, page, size);
    }
}
