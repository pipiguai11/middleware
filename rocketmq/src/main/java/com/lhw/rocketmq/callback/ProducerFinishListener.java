package com.lhw.rocketmq.callback;

import com.lhw.rocketmq.apply.AbstractProducer;

/**
 * @author ：linhw
 * @date ：21.10.29 15:10
 * @description：生产者任务结束监听器
 * @modified By：
 */
public class ProducerFinishListener implements FinishListener {

    private AbstractProducer producer;

    public ProducerFinishListener(AbstractProducer producer){
        this.producer = producer;
    }

    @Override
    public void finish(FinishCallback callback) {
        callback.callback(producer.getProducerName());
    }
}
