package com.lhw.rocketlog.producer;

import com.lhw.rocketbase.apply.AbstractProducer;
import com.lhw.rocketbase.base.Constant;
import com.lhw.rocketlog.constant.BaseConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.List;

/**
 * @author ：linhw
 * @date ：21.11.8 16:00
 * @description：日志抽象生产者
 * @modified By：
 */
public abstract class AbstractLogProducer extends AbstractProducer implements IProducer {

    protected DefaultMQProducer producer;

    public AbstractLogProducer(Class<?> clazz){
        super(clazz.getSimpleName());
        this.producer = new DefaultMQProducer(clazz.getSimpleName());
        this.producer.setNamesrvAddr(BaseConstant.DEFAULT_NAMESRV_ADDR);
    }

    @Override
    public void start() {
        try {
            producer.start();
        }catch (Exception e){
            System.out.println("生产者启动失败：" + e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        if (producer != null && producer.getNamesrvAddr() != null){
            producer.shutdown();
        }
        if (listener != null){
            finish();
        }
    }

    @Override
    public abstract void send(Message message);

    @Override
    public void sendAll(List<Message> messages){
        messages.forEach(this::send);
    }
}
