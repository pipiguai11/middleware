package com.lhw.pubsub;

import com.lhw.pubsub.config.RedisPubSubConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author ：linhw
 * @date ：22.10.11 16:41
 * @description：
 * @modified By：
 */
@SpringBootTest
public class RedisPubSubTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test() {
        redisTemplate.convertAndSend(RedisPubSubConfig.LISTENER_MESSAGE_TOPIC, "hello");
        System.out.println("发送成功");
    }

}
