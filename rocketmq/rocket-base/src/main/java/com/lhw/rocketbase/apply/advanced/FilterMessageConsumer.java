package com.lhw.rocketbase.apply.advanced;

import com.lhw.rocketbase.base.Constant;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

/**
 * @author ：linhw
 * @date ：21.11.2 09:55
 * @description：过滤消息消费者
 *
 *      在消费消息之前，先过滤一遍，只消费满足条件的消息
 *
 *      这里需要注意：因为Broker默认是不开启sql过滤的，因此如果我们需要用MessageSelector.bySql的方式去过滤的话，
 *      我们还需要在Broker的配置文件中添加【enablePropertyFilter=true】配置，然后重启即可，
 *      如果不开启配置的话，会报org.apache.rocketmq.client.exception.MQClientException: CODE: 1 DESC: The broker does not support consumer to filter message by SQL92
 *
 * @modified By：
 */
public class FilterMessageConsumer{

    public static void main(String[] args) {
//        simpleFilter();
        filterBySql();
    }

    private static void simpleFilter(){
        new Thread(new SimpleMessageFilter()).start();
    }

    private static void filterBySql(){
        new Thread(new FilterMessageBySql()).start();
    }

    private static class SimpleMessageFilter implements Runnable{

        @SneakyThrows
        @Override
        public void run() {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("filterMessageConsumer");
            consumer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
//            consumer.subscribe(Constant.Topic.MY_TOPIC,Constant.Tag.TAG_A);
//            consumer.subscribe(Constant.Topic.MY_TOPIC,Constant.Tag.TAG_C);
//            consumer.subscribe(Constant.Topic.MY_TOPIC,Constant.Tag.TAG_D);
            consumer.subscribe(Constant.Topic.MY_TOPIC,Constant.Tag.TAG_A_C_D);
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                msgs.forEach(m -> System.out.println("tag = " + m.getTags() + ", content : " + new String(m.getBody())));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            System.out.println("消费者启动");
        }
    }

    private static class FilterMessageBySql implements Runnable{

        @SneakyThrows
        @Override
        public void run() {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("filterMessageConsumer");
            consumer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
            //设置监听主题的同时定义sql过滤，只处理满足条件的消息
            consumer.subscribe(Constant.Topic.MY_TOPIC,MessageSelector.bySql("id >= 5 AND id <= 10"));
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                msgs.forEach(m -> System.out.println("properties : " + m.getProperties()
                        + ", queueId = " + m.getQueueId() + ", body = " + new String(m.getBody())));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            System.out.println("消费者启动");
        }
    }

}
