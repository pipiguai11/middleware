package com.lhw.rocketmq.simple.producer;

import com.lhw.rocketmq.callback.FinshListener;
import com.lhw.rocketmq.callback.ProducerFinshListener;
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
    protected FinshListener listener = new ProducerFinshListener(this);

}
