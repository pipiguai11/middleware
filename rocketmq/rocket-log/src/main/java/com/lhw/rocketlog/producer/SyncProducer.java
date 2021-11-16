package com.lhw.rocketlog.producer;

import com.lhw.rocketlog.constant.BaseConstant;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author ：linhw
 * @date ：21.11.8 15:32
 * @description：同步发送消息生产者
 * @modified By：
 */
@Component(BaseConstant.Producer.MY_SYNC_PRODUCER)
public class SyncProducer extends AbstractLogProducer implements IProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SyncProducer.class);

    public SyncProducer() {
        super(SyncProducer.class);
    }

    @PostConstruct
    public void postConstruce(){
        System.out.println("同步生产者初始化后置调用");
        ProducerManager manager = new ProducerManager();
        IProducer producer = manager.getProducerByBeanName(BaseConstant.Producer.MY_SYNC_PRODUCER);
        manager.startProducer(producer);

        AtomicInteger num = new AtomicInteger(0);
        while (true){
            try {
                Message message = new Message(getTopic(),getTag(),
                        ("生产者发送消息" + num.get()).getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.send(message);
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void send(Message message) {
        try {
            SendResult result = producer.send(message);
            System.out.println(result);
            if (result.getSendStatus() != SendStatus.SEND_OK){
                System.out.println("消息发送异常");
                //TODO ：处理失败消息，入库
            }
        }catch (MQClientException | InterruptedException | RemotingException | MQBrokerException e){
            e.printStackTrace();
        }
    }
}
