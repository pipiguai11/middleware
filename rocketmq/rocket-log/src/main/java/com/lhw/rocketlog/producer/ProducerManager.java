package com.lhw.rocketlog.producer;

import com.lhw.rocketlog.config.ApplicationManager;
import com.lhw.rocketlog.constant.BaseConstant;
import com.lhw.rocketlog.constant.ProducerEnum;


/**
 * @author ：linhw
 * @date ：21.11.8 15:33
 * @description：生产者管理器
 * @modified By：
 */
public class ProducerManager {

    public IProducer getProducerByBeanName(String producerBeanName){
        ProducerEnum.checkProducerExist(producerBeanName);
        return (IProducer)ApplicationManager.getBean(producerBeanName);
    }

    public IProducer getDefaultProducer(){
        IProducer producer = (IProducer)ApplicationManager.getBean(BaseConstant.Producer.MY_ASYNC_PRODUCER);
        producer.start();
        return producer;
    }

    public void startProducer(IProducer producer){
        producer.start();
    }

    public void shutdownProducer(IProducer producer){
        producer.shutdown();
    }

}
