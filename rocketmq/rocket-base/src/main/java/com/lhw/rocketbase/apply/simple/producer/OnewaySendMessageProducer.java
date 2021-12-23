package com.lhw.rocketbase.apply.simple.producer;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author ：linhw
 * @date ：21.10.29 17:12
 * @description：单向发送消息生产者
 * @modified By：
 */
public class OnewaySendMessageProducer extends AbstractProducer{

    public OnewaySendMessageProducer(){
        super("OnewaySendMessageProducer", Constant.Topic.ONE_WAY_TOPIC,Constant.Tag.TAG_C);
    }

    @SneakyThrows
    @Override
    public void runTask() {
        DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
        producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
        producer.start();
        System.out.println("生产者启动！");
        int i = 0;
        while (i < 10){
            Message message = new Message(getTopic(),getTag(),
                    ("生产者发送消息" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //单方面发送消息
            producer.sendOneway(message);
            i++;
            Thread.sleep(2000);
        }
        producer.shutdown();
    }

    public static void main(String[] args) {
        new Thread(new OnewaySendMessageProducer()).start();
    }

}
