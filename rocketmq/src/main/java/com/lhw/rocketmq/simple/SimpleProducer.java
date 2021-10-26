package com.lhw.rocketmq.simple;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author ：linhw
 * @date ：21.10.26 13:59
 * @description：简单的消息生产者
 * @modified By：
 */
public class SimpleProducer {

    public static void main(String[] args) {
        Thread thread = new Thread(new MessageTask());
        thread.start();
    }

    private static class MessageTask implements Runnable{
        @SneakyThrows
        @Override
        public void run() {
            // 实例化消息生产者Producer
            DefaultMQProducer producer = new DefaultMQProducer("myProducer");
            //设置通信地址
            producer.setNamesrvAddr("localhost:9876");
            //启动生产者
            producer.start();
            System.out.println("生产者启动！");
            int i = 0;
            while (true){
                //构建消息对象
                Message msg = new Message("MyTopic","TagA",
                        ("生产者发送消息" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                //生产者发送消息，并获取结果
                SendResult result = producer.send(msg);
                System.out.println(result);
                i++;
                if (i == 100){
                    break;
                }
                Thread.sleep(2000);
            }
            //停止消息生产者
            producer.shutdown();
        }
    }

}
