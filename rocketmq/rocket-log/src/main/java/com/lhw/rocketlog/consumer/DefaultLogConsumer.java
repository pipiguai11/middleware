package com.lhw.rocketlog.consumer;

import com.lhw.rocketlog.config.ApplicationManager;
import com.lhw.rocketlog.constant.BaseConstant;
import com.lhw.rocketlog.storer.IStroe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    @Autowired
    IStroe stroe;

    @PostConstruct
    public void postConstruct(){
        IConsumer consumer = (IConsumer)ApplicationManager.getBean(BaseConstant.Consumer.MY_DEFAULT_CONSUMER);
        registerListener(new DefaultConsumerListener(stroe));
        consumer.start();
    }

}
