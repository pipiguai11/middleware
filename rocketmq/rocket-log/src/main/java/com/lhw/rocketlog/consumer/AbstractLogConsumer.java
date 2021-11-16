package com.lhw.rocketlog.consumer;

import com.lhw.rocketlog.constant.BaseConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author ：linhw
 * @date ：21.11.15 17:20
 * @description：抽象日志消费者
 * @modified By：
 */
public abstract class AbstractLogConsumer implements IConsumer {

    private DefaultMQPushConsumer consumer;

    private String topic;
    private String tag;

    public AbstractLogConsumer(Class<?> clazz, String topic, String tag){
        this.topic = topic;
        this.tag = tag;
        try {
            consumer = new DefaultMQPushConsumer(clazz.getSimpleName());
            consumer.setNamesrvAddr(BaseConstant.DEFAULT_NAMESRV_ADDR);
            consumer.subscribe(this.topic,this.tag);
//            consumer.registerMessageListener(new DefaultConsumerListener());
        }catch (MQClientException e){
            e.printStackTrace();
        }
    }

    public AbstractLogConsumer(Class<?> clazz){
        this(clazz, BaseConstant.Consumer.DEFAULT_TOPIC, BaseConstant.Consumer.DEFAULT_TAG);
    }

    public void registerListener(MessageListenerConcurrently listenerConcurrently){
        consumer.registerMessageListener(listenerConcurrently);
    }

    @Override
    public void start() {
        try {
            consumer.start();
        }catch (MQClientException e){
            System.out.println("消费者启动失败，【" + e.getErrorMessage() + "】");
        }
    }

    @Override
    public void shutdown() {
        consumer.shutdown();
    }
}
