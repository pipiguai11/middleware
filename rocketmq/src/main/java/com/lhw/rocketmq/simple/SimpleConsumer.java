package com.lhw.rocketmq.simple;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Scanner;

/**
 * @author ：linhw
 * @date ：21.10.26 14:11
 * @description：简单的消息消费者
 * @modified By：
 */
public class SimpleConsumer {

    public static void main(String[] args) {
        Thread consumer = new Thread(new MessageConsumer());
        consumer.start();
    }

    private static class MessageConsumer implements Runnable{

        @SneakyThrows
        @Override
        public void run() {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("myConsumer");
            consumer.setNamesrvAddr("localhost:9876");
            consumer.subscribe("MyTopic","*");
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    System.out.println(Thread.currentThread().getName() + "线程消费了一个新的消息: 【" + msgs + "】");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
            System.out.println("消费者启动！");

            //阻塞住线程
            Scanner scanner = new Scanner(System.in);
            scanner.nextInt();
        }
    }

}
