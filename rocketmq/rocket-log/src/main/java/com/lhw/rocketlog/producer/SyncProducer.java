package com.lhw.rocketlog.producer;

import com.lhw.rocketlog.constant.BaseConstant;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;


/**
 * @author ：linhw
 * @date ：21.11.8 15:32
 * @description：同步发送消息生产者
 * @modified By：
 */
@Component(BaseConstant.Producer.MY_SYNC_PRODUCER)
public class SyncProducer extends AbstractLogProducer implements IProducer {

    public SyncProducer() {
        super(SyncProducer.class);
    }

    @Override
    public void send(Message message) {
        try {
            SendResult result = producer.send(message);
            if (result.getSendStatus() != SendStatus.SEND_OK){
                System.out.println("消息发送异常");
                //TODO ：处理失败消息，入库
            }
        }catch (MQClientException | InterruptedException | RemotingException | MQBrokerException e){
            e.printStackTrace();
        }
    }
}
