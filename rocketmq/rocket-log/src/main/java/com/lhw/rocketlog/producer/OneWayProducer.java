package com.lhw.rocketlog.producer;

import com.lhw.rocketlog.constant.BaseConstant;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

/**
 * @author ：linhw
 * @date ：21.11.8 15:33
 * @description：单向发送消息生产者
 * @modified By：
 */
@Component(BaseConstant.Producer.MY_ONE_WAY_PRODUCER)
public class OneWayProducer extends AbstractLogProducer implements IProducer {

    public OneWayProducer() {
        super(OneWayProducer.class);
    }

    @Override
    public void send(Message message) {
        try {
            producer.sendOneway(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
