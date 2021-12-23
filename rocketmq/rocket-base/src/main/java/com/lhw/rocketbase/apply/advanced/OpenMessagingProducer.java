package com.lhw.rocketbase.apply.advanced;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
import io.openmessaging.Future;
import io.openmessaging.Message;
import io.openmessaging.MessagingAccessPoint;
import io.openmessaging.OMS;
import io.openmessaging.producer.Producer;
import io.openmessaging.producer.SendResult;

import java.util.concurrent.CountDownLatch;

/**
 * @author ：linhw
 * @date ：21.11.5 10:14
 * @description：基于OpenMessaging访问RocketMQ
 * @modified By：
 */
public class OpenMessagingProducer extends AbstractProducer{

    public OpenMessagingProducer(){
        super("OpenMessagingProducer");
    }

    public static void main(String[] args) {
        new Thread(new OpenMessagingProducer()).start();
    }

    @Override
    public void runTask() {
        final MessagingAccessPoint messagingAccessPoint = OMS
                .getMessagingAccessPoint("oms:rocketmq://localhost:9876/default:default");
        final Producer producer = messagingAccessPoint.createProducer();
        producer.startup();
        System.out.printf("Producer startup OK%n");
        {
            Message message = producer.createBytesMessage(Constant.Topic.MY_TOPIC,"hello openMessaging by sync".getBytes());
            SendResult result = producer.send(message);
            System.out.println("同步消息发送成功，msgId ： " + result.messageId());
        }

        //定义一个线程执行计数器，当计数为0时，表示所有线程执行完毕，继续主线程的后续逻辑
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        {
            Message asyncMessage = producer.createBytesMessage(Constant.Topic.MY_TOPIC,"hello openMessaging by async".getBytes());
            final Future<SendResult> future = producer.sendAsync(asyncMessage);
            future.addListener(futureListener -> {
                if (futureListener.getThrowable() != null){
                    System.out.println("异步消息发送异常，error :" + futureListener.getThrowable().getMessage());
                }else {
                    System.out.println("消息发送成功，msgId ： " + futureListener.get().messageId());
                }
                //表示此线程执行完毕，执行countDown做计数器减一操作
                countDownLatch.countDown();
            });
        }

        {
            Message oneWayMessage = producer.createBytesMessage(Constant.Topic.MY_TOPIC,"hello openMessaging by oneWay".getBytes());
            producer.sendOneway(oneWayMessage);
            System.out.println("单向消息发送成功");
        }

        try {
            //等待所有线程执行完毕，当计数为0时说明所有线程都执行完了，这时就可以执行后面的逻辑了。
            countDownLatch.await();
            producer.shutdown();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
