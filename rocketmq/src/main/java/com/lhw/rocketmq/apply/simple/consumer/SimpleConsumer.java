package com.lhw.rocketmq.apply.simple.consumer;

import com.lhw.rocketmq.base.Constant;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

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
            //创建消费者对象
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("myConsumer");
            //设置通信地址
            consumer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
            //订阅消息
            consumer.subscribe(Constant.Topic.MY_TOPIC,Constant.Tag.ALL_TAG);
//            consumer.subscribe(Constant.Topic.ASYNC_TOPIC,Constant.Tag.ALL_TAG);
//            consumer.subscribe(Constant.Topic.ONE_WAY_TOPIC,Constant.Tag.ALL_TAG);
            //注册监听器，每当有一个消息准备被消费时，都会调用这个监听器（注册回调实现类来处理从broker拉取回来的消息）
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    System.out.println(Thread.currentThread().getName() + "线程消费了一个新的消息: 【" + msgs + "】");
                    System.out.println("消息内容为：【" + new String(msgs.get(0).getBody()) + "】");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            //消费者启动
            consumer.start();
            System.out.println("消费者启动！");
        }
    }

}
