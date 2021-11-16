package com.lhw.rocketlog.consumer;

import com.lhw.rocketlog.config.ApplicationManager;
import com.lhw.rocketlog.constant.BaseConstant;
import com.lhw.rocketlog.storer.IStroe;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author ：linhw
 * @date ：21.11.15 17:16
 * @description：默认消费者监听器
 * @modified By：
 */
public class DefaultConsumerListener implements MessageListenerConcurrently {

    private IStroe stroe;

    public DefaultConsumerListener(IStroe stroe){
        this.stroe = stroe;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        System.out.println(Thread.currentThread().getName() + "线程消费了一个新的消息: 【" + msgs + "】");
        System.out.println("消息内容为：【" + new String(msgs.get(0).getBody()) + "】");
        //数据落盘
        System.out.println(stroe);
        stroe.save(new String(msgs.get(0).getBody()));
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
