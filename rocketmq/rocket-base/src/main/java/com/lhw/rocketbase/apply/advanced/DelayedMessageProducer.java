package com.lhw.rocketbase.apply.advanced;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.Date;

/**
 * @author ：linhw
 * @date ：21.11.1 14:44
 * @description：延时消息生产者
 *
 *      这个需要测试消费者的话，直接启动SimpleConsumer即可，只需要确保topic匹配就行。
 *      设置延时等级：
 *          1 ：1秒后发送
 *          2 ：5秒后发送
 *          3 ：10秒后发送
 *          以此类推
 *      现在只支持（1s、 5s、 10s、 30s、 1m、 2m、 3m、 4m、 5m、 6m、 7m、 8m、 9m、 10m、 20m、 30m、 1h、 2h）
 *
 * @modified By：
 */
public class DelayedMessageProducer extends AbstractProducer{

    public DelayedMessageProducer(){
        super("DelayedMessageProducer");
    }

    public static void main(String[] args) {
        new Thread(new DelayedMessageProducer()).start();
    }

    @SneakyThrows
    @Override
    public void runTask() {
        DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
        producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
        producer.start();
        System.out.println("消息生产者启动");
        int totalMessage = 10;
        for (int i = 0 ; i < totalMessage ; i++){
            String msg = SIMPLE_DATE_FORMAT.format(new Date()) + " hello RocketMQ_" + i;
            Message message = new Message(getTopic() , msg.getBytes());
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)，现在只支持（1s、 5s、 10s、 30s、 1m、 2m、 3m、 4m、 5m、 6m、 7m、 8m、 9m、 10m、 20m、 30m、 1h、 2h）
            message.setDelayTimeLevel(3);
            SendResult result = producer.send(message);
            System.out.println(result);
        }

        producer.shutdown();
    }

}
