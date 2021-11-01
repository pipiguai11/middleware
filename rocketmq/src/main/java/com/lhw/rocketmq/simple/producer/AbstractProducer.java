package com.lhw.rocketmq.simple.producer;

import com.lhw.rocketmq.callback.FinishListener;
import com.lhw.rocketmq.callback.ProducerFinishListener;
import lombok.Data;

/**
 * @author ：linhw
 * @date ：21.10.29 15:13
 * @description：抽象生产者
 * @modified By：
 */
@Data
public class AbstractProducer {

    private String producerName;

    //监听器
    protected FinishListener listener = new ProducerFinishListener(this);

}
