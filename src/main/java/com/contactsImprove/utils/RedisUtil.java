package com.contactsImprove.utils;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	
	public static String NULL="-1null0";
	private static RedisTemplate<String, Object> redisTemplate;
	
	@Autowired(required = true)
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		RedisUtil.redisTemplate = redisTemplate;
	}

	public static void setValue(String key,Object value) {
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key ,(System.currentTimeMillis()+20*60*1000), TimeUnit.MILLISECONDS);
	}
	
	public static String getValue(String key) {
		String token = (String) redisTemplate.opsForValue().get(key);
		if(token==null) {
			return NULL;
		}
		return token;
	}
	
	public static void setValueByTime(String key,Object value, long time) {
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key ,time, TimeUnit.SECONDS);// time 单位秒
	}
	
	public static void delValue(String key) {
		redisTemplate.delete(key);
	}

}
