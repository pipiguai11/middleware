package com.lhw.rocketmq.callback;

import com.lhw.rocketmq.simple.producer.AbstractProducer;

/**
 * @author ：linhw
 * @date ：21.10.29 15:10
 * @description：生产者任务结束监听器
 * @modified By：
 */
public class ProducerFinshListener implements FinshListener {

    private AbstractProducer producer;

    public ProducerFinshListener(AbstractProducer producer){
        this.producer = producer;
    }

    @Override
    public void finsh(FinshCallback callback) {
        callback.callback(producer.getProducerName());
    }
}
