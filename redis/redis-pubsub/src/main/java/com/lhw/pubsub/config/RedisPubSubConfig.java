package com.lhw.pubsub.config;

import com.lhw.pubsub.adapter.ListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author ：linhw
 * @date ：22.10.11 16:05
 * @description：消息发布订阅配置
 * @modified By：
 */
@Configuration
public class RedisPubSubConfig {

    public static final String LISTENER_MESSAGE_TOPIC = "my_channel";

    /**
     * 实例化监听器
     * @return
     */
    @Bean
    public ListenerAdapter listenerAdapter() {
        return new ListenerAdapter();
    }

    /**
     * 封装一层监听器处理
     * @param adapter
     * @return
     */
    @Bean
    public MessageListenerAdapter adapter(ListenerAdapter adapter) {
        //传进去监听处理器的同时指定调用哪个方法去处理
        return new MessageListenerAdapter(adapter, "onMessage");
    }

    /**
     * 监听器配置
     * @param factory
     * @param adapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory,
                                                   MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        //添加监听器
        container.addMessageListener(adapter, new PatternTopic(LISTENER_MESSAGE_TOPIC));
        return container;
    }

}
