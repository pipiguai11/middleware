package com.lhw.rocketlog.consumer;

import com.lhw.rocketlog.constant.BaseConstant;
import org.springframework.stereotype.Component;

/**
 * @author ：linhw
 * @date ：21.11.15 17:39
 * @description：默认日志消费者
 * @modified By：
 */
@Component(BaseConstant.Consumer.MY_DEFAULT_CONSUMER)
public class DefaultLogConsumer extends AbstractLogConsumer implements IConsumer {

    public DefaultLogConsumer(){
        super(DefaultLogConsumer.class);
    }

}
