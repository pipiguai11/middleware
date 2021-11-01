package com.lhw.rocketmq.apply.advanced;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;

/**
 * @author ：linhw
 * @date ：21.11.1 13:56
 * @description：顺序消息消费者
 * @modified By：
 */
public class OrderMessageConsumer implements Runnable{

    public static void main(String[] args) {
        new Thread(new OrderMessageConsumer()).start();
    }

    @SneakyThrows
    @Override
    public void run() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderMessageConsumer");
        consumer.setNamesrvAddr("localhost:9876");
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //订阅MyTopic主题，且过滤标签
        consumer.subscribe("MyTopic", "*");
        consumer.setMessageListener(new MessageListenerOrderly() {
            Random random = new Random(3);

            @SneakyThrows
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);
                msgs.forEach(m -> System.out.println(Thread.currentThread().getName()
                        + " , queueId = " + m.getQueueId()
                        + " , content = " + new String(m.getBody())));

                //模拟业务处理
//                Thread.sleep(random.nextInt() * 1000);

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.println("顺序消息消费者启动");

    }
}
