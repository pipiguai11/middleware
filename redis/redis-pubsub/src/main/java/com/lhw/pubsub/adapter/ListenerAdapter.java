package com.lhw.pubsub.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author ：linhw
 * @date ：22.10.11 15:53
 * @description：监听器处理器
 * @modified By：
 */
public class ListenerAdapter implements MessageListener {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        @SuppressWarnings("unchecked")
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getDefaultSerializer();
        assert stringRedisSerializer != null;
        String messageStr = stringRedisSerializer.deserialize(message.getBody());
        System.out.println("消息内容：" + messageStr);
    }
}
