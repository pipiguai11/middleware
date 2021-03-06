package com.lhw.rocketbase.apply.simple.producer;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
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
public class SyncSendMessageProducer extends AbstractProducer{

    public SyncSendMessageProducer(String topic, String tag){
        super("syncSendMessageProducer",topic,tag);
    }

    public SyncSendMessageProducer(String topic){
        this(topic, Constant.Tag.TAG_A);
    }

    public SyncSendMessageProducer(){
        this(Constant.Topic.MY_TOPIC);
    }

    @SneakyThrows
    @Override
    public void runTask() {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(getProducerName());
        //设置通信地址
        producer.setNamesrvAddr(Constant.DEFAULT_NAMESRV_ADDR);
        //启动生产者
        producer.start();
        System.out.println("生产者启动！");
        int i = 0;
        while (true){
            //构建消息对象
            Message msg = new Message(getTopic(),getTag(),
                    ("生产者发送消息" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //生产者发送消息，并获取结果
            SendResult result = producer.send(msg);
            System.out.println(result);
            i++;
            if (i == 10){
                break;
            }
            Thread.sleep(2000);
        }
        //停止消息生产者
        producer.shutdown();
    }

    public static void main(String[] args) {
        new Thread(new SyncSendMessageProducer()).start();
    }

}
