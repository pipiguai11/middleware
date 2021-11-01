package com.lhw.rocketmq.apply.simple.producer;

import com.lhw.rocketmq.apply.AbstractProducer;
import com.lhw.rocketmq.base.Constant;
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
public class OnewaySendMessageProducer extends AbstractProducer implements Runnable {

    public OnewaySendMessageProducer(){
        super("OnewaySendMessageProducer","oneWayTopic","TagC");
    }

    @SneakyThrows
    @Override
    public void run() {
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
        finish();
    }

    public static void main(String[] args) {
        new Thread(new OnewaySendMessageProducer()).start();
    }

}
