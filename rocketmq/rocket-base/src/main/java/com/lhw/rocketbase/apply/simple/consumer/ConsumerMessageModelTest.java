package com.lhw.rocketbase.apply.simple.consumer;

import com.lhw.rocketbase.base.Constant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author ：linhw
 * @date ：22.10.24 16:16
 * @description：消费者消费模式（集群或广播）测试
 * @modified By：
 */
public class ConsumerMessageModelTest {

    public static void main(String[] args) throws MQClientException {
        //注意，注册的消费者组需要一致
        DefaultMQPushConsumer consumer1 = new DefaultMQPushConsumer(SimpleConsumer.consumerGroup);
        //监听的主题Topic和Tag也需要一致
        consumer1.subscribe(Constant.Topic.ASYNC_TOPIC,Constant.Tag.ALL_TAG);
        consumer1.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);

        //设置集群模式消费，默认也是集群的
        consumer1.setMessageModel(MessageModel.CLUSTERING);
        //另一种模式为广播消费
//        consumer1.setMessageModel(MessageModel.BROADCASTING);
        consumer1.registerMessageListener((MessageListenerConcurrently)(msgs, context) -> {
            System.out.println("消费者2消费了一个新的消息：【" + msgs + "】");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer1.start();
    }

}
