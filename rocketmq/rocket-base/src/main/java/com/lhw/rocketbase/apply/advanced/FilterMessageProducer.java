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
 * @date ：21.11.2 10:28
 * @description：过滤消息生产者
 *
 *      其中提供两种方式，一种是基于Tag做过滤，一种是基于属性Property
 *      1、基于Tag：每个消息只能绑定一个Tag，在生产者生产消息时绑定，消费者可以根据这个进行过滤
 *          基于Tag的方式这里就不实现了，可以直接复用OrderMessageProducer和FilterMessageConsumer.SimpleMessageFilter实现效果
 *      2、基于属性：生产者在生产消息时，可以通过message的putUserProperty方法设置属性，然后消费者可以根据这个进行过滤
 *
 * @modified By：
 */
public class FilterMessageProducer {

    public static void main(String[] args) {
        new Thread(new PropertyFilterMessageProducer()).start();
    }

    private static class PropertyFilterMessageProducer extends AbstractProducer implements Runnable{

        public PropertyFilterMessageProducer(){
            super("PropertyFilterMessageProducer");
        }

        @SneakyThrows
        @Override
        public void run() {
            DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
            producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
            producer.start();
            System.out.println("生产者启动");
            for (int i = 0 ; i < 15 ; i++){
                Message message = new Message(Constant.Topic.MY_TOPIC,
                        (SIMPLE_DATE_FORMAT.format(new Date()) + " hello_" + i).getBytes());
                //设置属性，用于消费者做过滤
                message.putUserProperty("id", String.valueOf(i));
                SendResult result = producer.send(message);
                System.out.println(result);
            }
            producer.shutdown();
            finish();
        }
    }

}
